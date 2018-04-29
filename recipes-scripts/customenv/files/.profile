if [ -f ${HOME}/.bashrc ]
then
	source ${HOME}/.bashrc
fi

iptables-restore < /home/root/rules.ipt

export COUNTRY=SE
