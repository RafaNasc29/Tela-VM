<?xml version="1.0" encoding="UTF-8"?>

        <?import javafx.scene.control.Button?>
        <?import javafx.scene.control.Menu?>
        <?import javafx.scene.control.MenuBar?>
        <?import javafx.scene.control.MenuItem?>
        <?import javafx.scene.control.RadioButton?>
        <?import javafx.scene.control.TableColumn?>
        <?import javafx.scene.control.TableView?>
        <?import javafx.scene.control.TextArea?>
        <?import javafx.scene.layout.AnchorPane?>
        <?import javafx.scene.layout.Pane?>
        <?import javafx.scene.text.Text?>
        <?import org.fxmisc.richtext.CodeArea?>


<AnchorPane fx:id="TopLevelElement" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="561.0" prefWidth="829.0" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.telavm.telavirtualm.Controller">




<MenuBar fx:id="Menubar" layoutY="2.0" prefHeight="25.0" prefWidth="829.0">

<Menu fx:id="MainMenu" mnemonicParsing="false" text="File">

<MenuItem fx:id="OpenFileMenuItem" mnemonicParsing="false" onAction="#openFile" text="Open File" />

</Menu>

</MenuBar>
<TableView layoutX="14.0" layoutY="65.0" prefHeight="310.0" prefWidth="512.0">
<columns>
<TableColumn prefWidth="75.0" text="Linha" />
<TableColumn prefWidth="75.0" text="Instrução" />
<TableColumn prefWidth="75.0" text="Atributo 1" />
<TableColumn prefWidth="75.0" text="Atributo 2" />
<TableColumn prefWidth="211.0" text="Comentário" />
</columns>
</TableView>
<TableView layoutX="579.0" layoutY="65.0" prefHeight="310.0" prefWidth="200.0">
<columns>
<TableColumn prefWidth="100.0" text="Endereço" />
<TableColumn prefWidth="100.0" text="Valor" />
</columns>
</TableView>
<TextArea layoutX="14.0" layoutY="419.0" prefHeight="99.0" prefWidth="209.0" />
<Text layoutX="14.0" layoutY="61.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Código de máquina" />
<Text layoutX="14.0" layoutY="415.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Saída de Dados" />
<Pane layoutX="320.0" layoutY="419.0" prefHeight="99.0" prefWidth="165.0">

<RadioButton fx:id="PassoButton" layoutX="6.0" layoutY="44.0" mnemonicParsing="false" text="Passo a Passo" />
<RadioButton fx:id="NormalButton" layoutX="6.0" layoutY="14.0" mnemonicParsing="false" text="Normal" />
<Text strokeType="OUTSIDE" strokeWidth="0.0" text="Mode de Execução" />
</Pane>
<Button layoutX="579.0" layoutY="451.0" mnemonicParsing="false" prefHeight="25.0" prefWidth="82.0" text="Executar" />
<Button layoutX="697.0" layoutY="451.0" mnemonicParsing="false" prefHeight="25.0" prefWidth="82.0" text="Parar" />
<Text strokeType="OUTSIDE" strokeWidth="0.0" text="Text" />



</AnchorPane>
