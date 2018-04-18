#include <QMessageBox>

#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_pushButton_clicked()
{
    QString name = ui->comboBox->currentText();
    QString number = ui->comboBox_2->currentText();

    QMessageBox::information(this, "Some awesome message box",
                             "You selected " + name +
                             " and the number " + number +
                             "!");
}

void MainWindow::on_pushButton_2_clicked()
{
    qApp->quit();
}
