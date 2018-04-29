#!/bin/bash

#
# Filename:			openvpn-setup.sh
#
# Author:			Santiago Pagola
# Brief:			This scripts sets up both an OpenVPN server from scratch and adds
#                   the possibility to add clients that will connect to it.
# Last modified:	mån  1 jan 2018 19:12:20 CET

help()
{
    echo "USAGE: $0 [args]"
    echo "[args]"
    echo -e "-h\tShow this help and exit"
    echo -e "-m mode\tSelect VPN mode:\tbridge\ttunnel"
    echo -e "-n name\tAdd new client"
    echo -e "-v\tShow script version and exit"
}

version()
{
    echo "$0, script version v$version"
}

home_dir="/home/root"
ca_dir="$home_dir/openvpn-ca"
client_conf_dir="$home_dir/client-configs"
client_file_dir="$client_conf_dir/files"
client_base_file="$client_conf_dir/base.conf"
openvpn_dir="/etc/openvpn"
sysctl_file="/etc/sysctl.conf"
vars_file="vars"
key_dir="keys"
ta_file="$key_dir/ta.key"
server_name="server"
server_conf_file="server.conf"
server_remote_address="81.233.36.89"
client_name="client1"
ufw_file="/etc/ufw/before.rules"
ufw_default_file="/etc/default/ufw"
make_config="$client_conf_dir/make_config.sh"
only_client=0
dev=""
port=""
proto=""
version="0.1"


echo "Welcome to the OpenVPN installation script."
echo "--------------------------------------------"

while getopts "hvmn:" OPT; do
    case $OPT in
        m)
            if [[ $OPTARG == "bridge" ]] || [[ $OPTARG == "tunnel" ]]; then
                if [[ $OPTARG == "tunnel" ]]; then
                    dev="tun"
                    port="1194"
                    proto="udp"
                else
                    dev="tap0"
                    port="443"
                    proto="tcp"
                fi
            else
                echo "Mode must be either \"tunnel\" or \"bridge\""
                help
                exit 1
            fi
            ;;
        n)
            only_client=1
            client_name=$OPTARG
            ;;
        h)
            help
            exit 0
            ;;
        v)
            version
            exit 0
            ;;
        \?)
            help
            exit 1
            ;;
        *)
            echo "Unknown option -- \"$OPTIND\"" >& /dev/null
            exit 1
            ;;
    esac
done
if [[ -z "$dev" ]]; then
    while true; do
        echo -e "1\tIP TUNNEL mode"
        echo -e "2\tBRIDGE mode"
        echo -n "Mode choice missing. Which mode? [1|2, default=1] : "
        read which
        case $which in
            1|"")
                dev="tun"
                port="1194"
                proto="udp"
                break;
                ;;
            2)
                dev="tap0"
                port="443"
                proto="tcp"
                break;
                ;;
            *)
                echo "Wrong option" >& /dev/null
        esac
    done
fi
echo "New client name will be: $client_name, interface=$dev"

if [ $only_client -eq 0 ]; then
    echo "First copying easy-rsa utils to ~ -> ~/openvpn-ca"
    cp -r /usr/share/easy-rsa/ $ca_dir 

    cd $ca_dir
    echo "Current directory is: `pwd`"

    # vars_file=vars
    echo -n "Substituting values in $vars_file ... "
    sed -e 's/\(.*KEY_COUNTRY=\"\).*\(\"\)/\1SE\2/' -i $vars_file
    sed -e 's/\(.*KEY_PROVINCE=\"\).*\(\"\)/\1Ostergottland\2/' -i $vars_file
    sed -e 's/\(.*KEY_CITY=\"\).*\(\"\)/\1Linkoping\2/' -i $vars_file
    sed -e 's/\(.*KEY_ORG=\"\).*\(\"\)/\1Mysland\2/' -i $vars_file
    sed -e 's/\(.*KEY_EMAIL=\"\).*\(\"\)/\1santipagola@gmail.com\2/' -i $vars_file
    sed -e 's/\(.*KEY_OU=\"\).*\(\"\)/\1WBD\2/' -i $vars_file
    sed -e 's/\(.*KEY_NAME=\"\).*\(\"\)/\1'$server_name'\2/' -i $vars_file
    echo "Done."

    echo -n "Sourcing vars ... "
    source vars >& /dev/null
    ./clean-all >& /dev/null
    echo "Done."

    echo "Building root CA ... "
    echo "(Just enter through the prompts)"
    ./build-ca 
    echo "Done."

    echo "Building server certificate, key and encryption files ..."
    echo "(Just enter through the prompts. Say 'y' on the two questions at the end)"
    ./build-key-server $server_name
    echo "Done."

    echo -n "Building Diffie-Hellman keys ... "
    ./build-dh >& /dev/null
    echo "Done."

    echo -n "Generating HMAC signature under $ta_file ... "
    openvpn --genkey --secret $ta_file
    echo "Done."
fi

echo "Generating client certificate - key pairs ..."
echo "(Just enter through the prompts. Say 'y' on the two questions at the end)"
cd $ca_dir
./build-key-pass $client_name
echo "Done."

if [ $only_client -eq 0 ]; then
    echo -e "\n\nMoving on to real configuration!"
    echo -n "Copying server files ... "
    cp $key_dir/{ca.crt,server.crt,server.key,ta.key,dh2048.pem} $openvpn_dir
    echo "Done."

    echo -n "Downloading sample server configuration file ... "
    wget https://raw.githubusercontent.com/OpenVPN/openvpn/master/sample/sample-config-files/server.conf >& /dev/null
    # Simple check: file was found
    if [ $? -ne 0 ]; then
        echo "Fail."
        echo "Sample file was not found on Github repository. Try to get one yourself from openvpn.org"
        exit 1
    fi
    cp $server_conf_file $openvpn_dir
    echo "Done."
    echo -n "Substituting some values in $server_conf_file ... "
    LINE=$(\grep -n '^tls-auth' $server_conf_file | cut -d: -f1)
    let LINE=$LINE+1
    sed "${LINE}i key-direction 0" -i $server_conf_file
    sed -e 's/^\(cipher\s\+\).*/\1AES-128-CBC\nauth\ SHA256/' -i $server_conf_file
    sed -e 's/^.\(user\)\s\+.*$/\1\ nobody/' -i $server_conf_file
    sed -e 's/^.\(group\)\s\+.*$/\1\ nogroup/' -i $server_conf_file
    sed -e 's/^.*\(push\s\+\"redirect\-gateway\ def1\ bypass\-dhcp\"\)/\1/' -i $server_conf_file
    sed -e 's/^.\(push\s\+\"dhcp\-option\ DNS\ \).*/\1208\.67\.222\.222\"/' -i $server_conf_file
    sed -e 's/^.\(push\s\+\"dhcp\-option\ DNS\ \).*/\1208\.67\.220\.220\"/' -i $server_conf_file
    # Change the port and protocol
    sed -e 's/^port\s\+[0-9]*/port\ '$port'/' -i $server_conf_file
    sed -e 's/^\(proto\s\+\)[a-z]*/\1'$proto'/' -i $server_conf_file
    # Set MTU to 1500 (+32)
    echo -e "\n# Set MTU of 1500\ntun-mtu 1500\ntun-mtu-extra 32" >> $server_conf_file
    echo "Done."

    # Edit the sysctl.conf file
    echo "Changing ip forward policy under $sysctl_file ... "
    sed -e 's/.*\(net\.ipv4\.ip\_forward\=1\).*/\1/g' -i $sysctl_file 
    # See that the above command worked:
    sysctl -p | grep -qE 'net\.ipv4\.ip_forward=1'
    if [ $? -eq 0 ]; then
        echo "Done."
    else
        echo "Fail."
    fi

    # Edit the firewall rules
    echo -n "Changing firewall rules in $ufw_file ... "
    sed "10i #######################################################" -i $ufw_file
    sed "11i # START OPENVPN RULES" -i $ufw_file
    sed "12i #######################################################" -i $ufw_file
    sed "13i # NAT table rules" -i $ufw_file
    sed "14i *nat" -i $ufw_file
    sed "15i :POSTROUTING ACCEPT [0:0]" -i $ufw_file
    sed "16i # Allow traffic from OpenVPN client to eth0" -i $ufw_file
    sed "17i -A POSTROUTING -s 10.8.0.0/8 -o eth0 -j MASQUERADE" -i $ufw_file
    sed "18i COMMIT" -i $ufw_file
    sed "19i #######################################################" -i $ufw_file
    sed "20i # END OPENVPN RULES" -i $ufw_file
    sed "21i #######################################################" -i $ufw_file
    echo "Done."

    # Change default fwd policy
    echo -n "Changing default forward policy in $ufw_default_file ... "
    sed -e 's/\(DEFAULT_FORWARD_POLICY=\).*/\1\"ACCEPT\"/' -i $ufw_default_file
    echo "Done."

    # Allow tcp443 + some other interesting ports in firewall
    ufw allow 443/tcp
    ufw allow 1194/udp
    ufw allow 22/tcp
    ufw allow 3423/tcp
    # Disable & reenable it
    ufw disable
    ufw enable

    # Edit add some rules to iptables regarding br0 and tap0 interfaces
    if [[ $dev == "tap0" ]]; then
        iptables -A INPUT -i tap0 -j ACCEPT
        iptables -A INPUT -i br0 -j ACCEPT
        iptables -A FORWARD -i br0 -j ACCEPT
    fi

    service_name="$server_name"-"$dev"
    echo -n "Starting systemctl service \"$service_name\"..."
    systemctl start openvpn@$service_name >& /dev/null
    if [ $? -ne 0 ]; then
        echo "Fail."
    else
        echo "Done."
        # Enable only if succees
        systemctl enable openvpn@$service_name
    fi
fi

# Create client config infrastructure
echo -n "Creating client configuration infrastructure ... "
test -d $client_file_dir ||
{
    mkdir -p $client_file_dir
    chmod 700 $client_file_dir 
}
cd $client_conf_dir
wget https://raw.githubusercontent.com/OpenVPN/openvpn/master/sample/sample-config-files/client.conf -O $client_name.conf >& /dev/null
if [ $? -ne 0 ]; then
    echo "Fail."
    echo "Sample file was not found on Github repository. Adding custom with defaults"

    cat << EOF > $client_name.conf
client
;dev tap
dev tun
;dev-node MyTap
;proto tcp
proto udp
remote my-server-1 1194
;remote my-server-2 1194
;remote-random
resolv-retry infinite
nobind
;user nobody
;group nogroup 
persist-key
persist-tun
;http-proxy-retry # retry on connection failures
;http-proxy [proxy server] [proxy port #]
;mute-replay-warnings
ca ca.crt
cert client.crt
key client.key
remote-cert-tls server
tls-auth ta.key 1
cipher AES-256-CBC
verb 3
;mute 20
EOF
fi

# Rename it
mv $client_name.conf $client_base_file

echo "Done"
echo -n "Changing some configuration on $client_base_file ... "
sed -e 's/^remote\s\+.*/remote '$server_remote_address' '$port'/' -i $client_base_file
sed -e 's/^\(proto\s\+\)[a-z]*/\1'$proto'/' -i $client_base_file
sed -e 's/^\(dev\s\+\)[a-z]*/\1'$dev'/' -i $client_base_file
sed -e 's/^.\(user\)\s\+.*$/\1\ nobody/' -i $client_base_file
sed -e 's/^.\(group\)\s\+.*$/\1\ nogroup/' -i $client_base_file
sed -e 's/^\(cipher\s\+\).*/\1AES-128-CBC\nauth\ SHA256/' -i $client_base_file
sed -e 's/^\(ca\ [a-z|A-Z|0-9]*\.crt\)/# \1/' -i $client_base_file
sed -e 's/^\(cert\ [a-z|A-Z|0-9]*\.crt\)/# \1/' -i $client_base_file
sed -e 's/^\(key\ [a-z|A-Z|0-9]*\.key\)/# \1/' -i $client_base_file
sed -e 's/^\(tls\-auth\ [a-z|A-Z|0-9]*\.key\ 1\)/# \1/' -i $client_base_file
# Append key-direction
echo -e "\nkey-direction 1" >> $client_base_file
echo >> $client_base_file
echo "# script-security 2" >> $client_base_file
echo "# up /etc/openvpn/update-resolv-conf" >> $client_base_file
echo "# down /etc/openvpn/update-resolv-conf" >> $client_base_file
# Set MTU to 1500 (+32)
echo -e "\n# Set MTU of 1500\ntun-mtu 1500\ntun-mtu-extra 32" >> $client_base_file
# Don't cache any passwords
echo -e "\n# No password cache\nauth-nocache" >> $client_base_file
echo "Done."

echo -n "Generating client .ovpn profile ... "
KEY_DIR=~/openvpn-ca/keys
OUTPUT_DIR=~/client-configs/files
BASE_CONFIG=~/client-configs/base.conf
OVPN_PROFILE=$OUTPUT_DIR/$client_name.ovpn

cat $BASE_CONFIG > $OVPN_PROFILE
echo "<ca>" >> $OVPN_PROFILE
cat "$KEY_DIR/ca.crt" >> $OVPN_PROFILE
echo -e "</ca>\n<cert>" >> $OVPN_PROFILE
cat $KEY_DIR/$client_name.crt >> $OVPN_PROFILE
echo -e "</cert>\n<key>" >> $OVPN_PROFILE
cat $KEY_DIR/$client_name.key >> $OVPN_PROFILE
echo -e "</key>\n<tls-auth>" >> $OVPN_PROFILE
cat $KEY_DIR/ta.key >> $OVPN_PROFILE
echo -e "</tls-auth>" >> $OVPN_PROFILE
echo "Done."
echo "$OVPN_PROFILE profile was generated under $OUTPUT_DIR"
echo -e "\n\nDone!"

# Ask if send to ubuntu-hp
while true; do
echo -ne "\nWould you like to send it to ubuntu-hp? [default=y] : "
read send
case $send in
    y|Y|"")
	    scp $OVPN_PROFILE eiger824@192.168.1.100:~
	    break
	    ;;
    n|N)
	    echo "Skipping ..."
	    break
	    ;;
    *)
	    echo "Wrong option"
	    ;;
	    
esac
done
