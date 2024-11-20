package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Livros {

    public static JComboBox operacao;
    public static JButton realizar , voltar , selecionar;
    public static int idBibliotecaEscolhida;
    public static JPanel container;
    public static JTextField InpId , InpTitulo;

   /* public static void main(String[] args){
        montar();
    }*/

    public static void montar(){



        InpTitulo = new JTextField();
        InpId = new JTextField();   // inicializar

        container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // 10px de espaçamento entre os componentes
        realizar = new JButton();
        realizar.setText("REALIZAR");

        voltar = new JButton();
        voltar.setText("VOLTAR");

        selecionar = new JButton();
        selecionar.setText("SELECIONAR");
        selecionar.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 mostrarInputs();
             }
        });

        String[] acoes;
        acoes = new String[]{"Buscar", "Incluir", "Excluir", "Alterar"};

        operacao = new JComboBox<>(acoes);

        JPanel panelCampos = new JPanel();
        //panelCampos.setLayout(new GridLayout(2 ,4, 5, 5)); // 5 linhas, 2 c
        panelCampos.setPreferredSize(new Dimension(699 , 414));
        panelCampos.add(operacao , BorderLayout.SOUTH);
        panelCampos.add(realizar, BorderLayout.SOUTH);  //ALGUEM MANDA ELES IREM PARA O SUL CARA
        panelCampos.add(voltar, BorderLayout.SOUTH);
        panelCampos.add(selecionar, BorderLayout.SOUTH);

        container.add(panelCampos , BorderLayout.CENTER);
    }

    public static void setIdBibliotecaEscolhida(Login id){
        idBibliotecaEscolhida = Integer.parseInt(id.toString());
    }

    public static void mostrarInputs(){
        if (operacao.getSelectedItem().toString() == "Buscar"){
            JLabel id = new JLabel("Digite o id do livro: ");
            JLabel titulo = new JLabel("Digite o título do livro: ");
            container.add(id);
            container.add(InpId);
            container.add(titulo);
            container.add(InpTitulo);
        }
        else if (operacao.getSelectedItem().toString() == "Incluir") {
            JLabel id = new JLabel("Digite o id do livro: ");
            JLabel titulo = new JLabel("Digite o título do livro: ");
            JLabel idAutor = new JLabel("Digite o id do autor: ");
            JLabel idArea = new JLabel("Digite o id da área que o livro pertence: ");
            JTextField inputid = new JTextField();
            JTextField inputtitulo = new JTextField();
            JTextField inputautor = new JTextField();
            JTextField inputarea = new JTextField();
            container.add(id);
            container.add(inputid);
            container.add(titulo);
            container.add(inputtitulo);
            container.add(idAutor);
            container.add(inputautor);
            container.add(idArea);
            container.add(inputarea);
        }
        else if(operacao.getSelectedItem().toString() == "Excluir"){
            JLabel LId = new JLabel("Digite o id do livro: ");
            JLabel LTitulo = new JLabel("Digite o título do livro: ");
            JTextField id = new JTextField();
            JTextField titulo = new JTextField();
            container.add(LId);
            container.add(id);
            container.add(LTitulo);
            container.add(titulo);
            //depois fazer um if para ver se algum JTExtielf está vazio
            //e fazer o select com bases nos que estão preenchidos
        }
        else if (operacao.getSelectedItem().toString() == "Alterar") {
            String[] dados = new String[]{"Id livro" , "Título" , "Id autor" , "Id área"};
            JLabel whereAntigo = new JLabel("Qual é o dado de referência?");
            JComboBox cbxAntigo = new JComboBox(dados);
            JLabel LDadoAntigo = new JLabel("Digite o dado de referência: ");
            JTextField dadoAntigo = new JTextField();
            JLabel qualAlterar = new JLabel("Qual dado quer alterar?");
            JComboBox cbxAlterar = new JComboBox(dados);
            JLabel LNovo = new JLabel("Qual é o novo dado?");
            JTextField novoDado = new JTextField();
            container.add(whereAntigo);
            container.add(cbxAntigo);
            container.add(LDadoAntigo);
            container.add(dadoAntigo);
            container.add(qualAlterar);
            container.add(cbxAlterar);
            container.add(LNovo);
            container.add(novoDado);
            //tem q pegar todos esses dados e passar para realizar a consulta no sql e dps mostrar

        }
    }

    public static JPanel realizarTudo(){
        montar();
        container.setLayout(new FlowLayout(FlowLayout.LEFT)); // Alinha os componentes à esquerda de maneira compacta
        container.setSize(300,200); // Limita o tamanho do painel
        return container;
    }

}
