/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.duelo_equacoes.telas;

import br.com.duelo_equacoes.dal.ModeloConexao;
import static br.com.duelo_equacoes.telas.TelaCadJogador.nivel;
import br.com.duelo_equacoes_classes.font;
import br.com.duelo_equacoes_classes.mouse;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Julimar
 */
public class TelaPerguntas2 extends javax.swing.JFrame {

    // ==========================================================
    // VARIÁVEIS GLOBAIS
    // ==========================================================
    int n1 = 0, n2 = 0, operacao = 0, pontos = 0, alternativas = 0;
    float resultado = 0, repostaJogador = 0;
    int tempoRestante = 5;
    String respostaCertaGlobal = "";
    javax.swing.Timer cronometro;
    font f = new font();
    Random rand = new Random();

    // ==========================================================
    // CONSTRUTOR: PREPARAÇÃO DA TELA
    // Inicia os componentes, aplica o design visual e dá o "play" no jogo.
    // ==========================================================
    public TelaPerguntas2() {
        initComponents();
        Font fontePersonalizada = f.carregarFonte(40f);
        Font fontePersonalizada2 = f.carregarFonte(60f);
        btAlternativa1.setFont(fontePersonalizada);
        btAlternativa2.setFont(fontePersonalizada);
        lblExp1.setFont(fontePersonalizada2);
        lblExp2.setFont(fontePersonalizada2);
        lblSinal.setFont(fontePersonalizada2);
        lblcont.setFont(fontePersonalizada2);
        Color corPadrao = new Color(0, 96, 57);
        Color corDestaque = new Color(166, 137, 83);
        mouse.corMouse(btAlternativa1, corPadrao, corDestaque);
        mouse.corMouse(btAlternativa2, corPadrao, corDestaque);
        posicaoAlternativa();
        iniciarCronometro();
    }

    // ==========================================================
    // SORTEIO DE NÚMEROS
    // Define a base (n1) e o expoente (n2) de 1 a 8 e mostra na tela.
    // ==========================================================
    public void aleatorio_numeros() {
        alternativas = rand.nextInt(1) + 1;
        n1 = rand.nextInt(8) + 1;
        n2 = rand.nextInt(8) + 1;
        lblExp1.setText(String.valueOf(n1));
        lblExp2.setText(String.valueOf(n2));
    }

    // ==========================================================
    // CALCULADORA DO JOGO
    // Faz a conta real de potenciação (n1 elevado a n2) e formata o texto.
    // ==========================================================
    public String respostaCorreta() {
        aleatorio_numeros();
        resultado = (float) Math.pow(n1, n2);
        // Mantém a sua excelente lógica de formatação visual!
        if (resultado == (int) resultado) {
            return String.valueOf((int) resultado);
        } else {
            return String.valueOf(resultado).replace(".", ",");
        }
    }

    // ==========================================================
    // GERENCIADOR DO TEMPO (5 SEGUNDOS)
    // Se chegar a zero, para o relógio sozinho e chama o Game Over.
    // ==========================================================
    public void iniciarCronometro() {
        tempoRestante = 5;
        lblcont.setText(String.valueOf(tempoRestante));
        // Cria o relógio na memória UMA ÚNICA VEZ
        if (cronometro == null) {
            cronometro = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    tempoRestante--;
                    lblcont.setText(String.valueOf(tempoRestante));
                    if (tempoRestante <= 0) {
                        // Para o relógio com segurança e chama o Game Over
                        cronometro.stop();
                        gameOver("O TEMPO ACABOU, VOCÊ DEMOROU MUITO!");
                    }
                }
            });
        }

        // A MÁGICA: Em vez de .start(), usamos o .restart()
        // Isso desliga qualquer contagem antiga e inicia do 5 perfeitamente!
        cronometro.restart();
    }

    // ==========================================================
    // FIM DE JOGO
    // Trava tudo, exibe os pontos, salva no banco e volta ao menu principal.
    // ==========================================================
    public void gameOver(String motivoDerrota) {
        if (cronometro != null) {
            cronometro.stop();
        }
        JOptionPane.showMessageDialog(this, motivoDerrota + "\n\nFIM DE JOGO!\nSUA PONTUAÇÃO FINAL FOI: " + pontos, "GAME OVER", JOptionPane.WARNING_MESSAGE);
        salvar();
        TelaInicio inicio = new TelaInicio();
        inicio.setVisible(true);
        this.dispose();
    }

    // ==========================================================
    // BANCO DE DADOS
    // Guarda a pontuação atual do jogador na tabela de forma segura.
    // ==========================================================
    public void salvar() {
        String sql = "UPDATE tbjogador SET pontos = ? WHERE id = ?";
        if (TelaCadJogador.idJogadorAtual == 0) {
            JOptionPane.showMessageDialog(null, "ID do jogador não encontrato", "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection conexao = ModeloConexao.conectar();
                java.sql.PreparedStatement pst = conexao.prepareStatement(sql)) {
            pst.setInt(1, pontos);
            pst.setInt(2, TelaCadJogador.idJogadorAtual);
            int linhaAfetada = pst.executeUpdate();
            if (linhaAfetada > 0) {
                System.out.println("Pontuação: " + pontos + " do ID: " + TelaCadJogador.idJogadorAtual + " salvo com sucesso");
            } else {
                System.out.println("ERRO AO SALVAR");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================================
    // JUIZ DA PARTIDA (BOTÕES)
    // Verifica se o botão clicado tem o texto igual à resposta certa.
    // ==========================================================
    public void checaAcerto(String textoBotao) {
        if (textoBotao.equals(respostaCertaGlobal)) {
            JOptionPane.showMessageDialog(this, "Acertou");
            pontos += 1;
            lblPontos.setText(String.valueOf(pontos));
        } else {
            gameOver("VOCê ESCOLHEU A RESPOSTA ERRADA! A CERT ERA:" + respostaCertaGlobal);
        }
        iniciarCronometro();
        posicaoAlternativa();
    }

    // ==========================================================
    // O MAESTRO DA RODADA
    // Chama a conta nova, gera a alternativa falsa e embaralha os 2 botões.
    // ==========================================================
    public void posicaoAlternativa() {
        aleatorio_numeros();
        String respostaFormatada = respostaCorreta();
        respostaCertaGlobal = respostaFormatada;
        float erro1 = 0f;
        erro1 = (float) Math.pow(n1, n2) + 10;
        if (erro1 == resultado) {
            erro1 = resultado + n1;
        }
        String textoErro1;
        if (erro1 == (int) erro1) {
            textoErro1 = String.valueOf((int) erro1);
        } else {
            textoErro1 = String.valueOf(erro1).replace(".", ",");
        }
        java.util.Random rand = new java.util.Random();
        alternativas = rand.nextInt(2) + 1;
        switch (alternativas) {
            case 1:
                btAlternativa1.setText(respostaFormatada);
                btAlternativa2.setText(textoErro1);
                break;

            case 2:
                // Resposta certa no Botão 2
                btAlternativa1.setText(textoErro1);
                btAlternativa2.setText(respostaFormatada);
                break;
        }
    }

    // ==========================================================
    // DESTRUIÇÃO SEGURA DA TELA
    // ==========================================================
    @Override
    public void dispose() {
        // Trava de segurança máxima: se a tela morrer, o relógio morre junto!
        if (cronometro != null) {
            cronometro.stop();
            cronometro = null;
        }
        super.dispose(); // Executa o fechamento padrão do Java
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblNivel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblPontos = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblExp1 = new javax.swing.JLabel();
        lblExp2 = new javax.swing.JLabel();
        lblSinal = new javax.swing.JLabel();
        btAlternativa1 = new javax.swing.JButton();
        btAlternativa2 = new javax.swing.JButton();
        lblcont = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Dificuldade:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 23, -1, -1));

        lblNivel.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblNivel.setForeground(new java.awt.Color(255, 255, 255));
        lblNivel.setText("-");
        getContentPane().add(lblNivel, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 23, 102, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Pontos:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 23, -1, -1));

        lblPontos.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblPontos.setForeground(new java.awt.Color(255, 255, 255));
        lblPontos.setText("-");
        getContentPane().add(lblPontos, new org.netbeans.lib.awtextra.AbsoluteConstraints(392, 23, 97, -1));

        jPanel1.setBackground(new java.awt.Color(0, 96, 57));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pergunta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        lblExp1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblExp1.setForeground(new java.awt.Color(255, 255, 255));
        lblExp1.setText("N1");

        lblExp2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblExp2.setForeground(new java.awt.Color(255, 255, 255));
        lblExp2.setText("N2");

        lblSinal.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblSinal.setForeground(new java.awt.Color(255, 255, 255));
        lblSinal.setText("?");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(209, 209, 209)
                        .addComponent(lblExp2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(lblExp1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(lblSinal)
                .addContainerGap(167, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(lblExp2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExp1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSinal))
                .addGap(92, 92, 92))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 88, -1, -1));

        btAlternativa1.setForeground(new java.awt.Color(255, 255, 255));
        btAlternativa1.setMaximumSize(new java.awt.Dimension(258, 63));
        btAlternativa1.setMinimumSize(new java.awt.Dimension(258, 63));
        btAlternativa1.setPreferredSize(new java.awt.Dimension(258, 63));
        btAlternativa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAlternativa1ActionPerformed(evt);
            }
        });
        getContentPane().add(btAlternativa1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 450, -1, -1));

        btAlternativa2.setForeground(new java.awt.Color(255, 255, 255));
        btAlternativa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAlternativa2ActionPerformed(evt);
            }
        });
        getContentPane().add(btAlternativa2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 570, 258, 63));

        lblcont.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblcont.setForeground(new java.awt.Color(255, 255, 255));
        lblcont.setText("1");
        getContentPane().add(lblcont, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 670, 40, 70));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/duelo_equacoes/img/Bg.jpg"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -1, 500, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btAlternativa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAlternativa1ActionPerformed
        checaAcerto(btAlternativa1.getText());
    }//GEN-LAST:event_btAlternativa1ActionPerformed

    private void btAlternativa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAlternativa2ActionPerformed
        checaAcerto(btAlternativa2.getText());
    }//GEN-LAST:event_btAlternativa2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPerguntas2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPerguntas2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPerguntas2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPerguntas2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPerguntas2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAlternativa1;
    private javax.swing.JButton btAlternativa2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblExp1;
    private javax.swing.JLabel lblExp2;
    private javax.swing.JLabel lblNivel;
    private javax.swing.JLabel lblPontos;
    private javax.swing.JLabel lblSinal;
    private javax.swing.JLabel lblcont;
    // End of variables declaration//GEN-END:variables
}
