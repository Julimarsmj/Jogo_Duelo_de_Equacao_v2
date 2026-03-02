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
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Julimar
 */
public class TelaPerguntas extends javax.swing.JFrame {

    // ==========================================================
    // VARIÁVEIS GLOBAIS
    // ==========================================================
    font f = new font();
    Random rand = new Random();
    int n1 = 0, n2 = 0, operacao = 0, pontos = 0, alternativas = 0;
    float resultado = 0, repostaJogador = 0;
    int variacaoDezena = 0, variacaoUnidade = 0;
    int tempoRestante = 0;
    String respostaCertaGlobal = "";
    javax.swing.Timer cronometro;

    // Construtor: Prepara o visual da tela ao abrir (fontes, cores e efeitos) 
    // e dá o pontapé inicial sorteando a primeira conta e ligando o relógio.
    public TelaPerguntas() {
        initComponents();
        Font fontePersonalizada = f.carregarFonte(40f);
        Font fontePersonalizada2 = f.carregarFonte(60f);
        lblN1.setFont(fontePersonalizada2);
        lblN2.setFont(fontePersonalizada2);
        lblSinal.setFont(fontePersonalizada2);
        lblInterrogacao.setFont(fontePersonalizada2);
        lblcont.setFont(fontePersonalizada2);
        btResposta1.setFont(fontePersonalizada);
        btResposta2.setFont(fontePersonalizada);
        btResposta3.setFont(fontePersonalizada);
        Color corPadrao = new Color(0, 96, 57);
        Color corDestaque = new Color(166, 137, 83);
        mouse.corMouse(btResposta1, corPadrao, corDestaque);
        mouse.corMouse(btResposta2, corPadrao, corDestaque);
        mouse.corMouse(btResposta3, corPadrao, corDestaque);
        posicaoAlternativa();
        iniciarCronometro();
    }

    // Sorteia a operação matemática e os números da conta, 
    // respeitando os limites do nível de dificuldade (Fácil ou Médio).
    public void aleatorio_numeros() {
        operacao = rand.nextInt(4) + 1;
        alternativas = rand.nextInt(3) + 1;
        if (nivel.equals("Fácil")) {
            n1 = rand.nextInt(10) + 1;
            n2 = rand.nextInt(10) + 1;
        } else if (nivel.equals("Médio")) {
            n1 = rand.nextInt(11) + 10;
            n2 = rand.nextInt(11) + 10;
        }
        lblN1.setText(String.valueOf(n1));
        lblN2.setText(String.valueOf(n2));
    }

    // Atualiza os textos na tela para mostrar a conta visualmente para o jogador,
    // colocando o sinal correto (+, -, x ou /) dependendo do sorteio.
    public void apresentacaoOperacao() {
        aleatorio_numeros();
        if (nivel.equals("Fácil") || nivel.equals("Médio")) {
            if (operacao == 1) {
                lblN1.setText(String.valueOf(n1));
                lblSinal.setText(" + ");
                lblN2.setText(String.valueOf(n2));
            } else if (operacao == 2) {
                lblN1.setText(String.valueOf(n1));
                lblSinal.setText(" - ");
                lblN2.setText(String.valueOf(n2));
            } else if (operacao == 3) {
                lblN1.setText(String.valueOf(n1));
                lblSinal.setText(" x ");
                lblN2.setText(String.valueOf(n2));
            } else {
                lblN1.setText(String.valueOf(n1));
                lblSinal.setText(" / ");
                lblN2.setText(String.valueOf(n2));
            }
        }

    }

    // Resolve a conta matematicamente para descobrir o resultado real
    // e formata o texto para ficar bonito (tira o .0 ou põe vírgula no lugar do ponto).
    public String respostaCorreta() {
        switch (operacao) {
            case 1:
                resultado = n1 + n2;
                break;
            case 2:
                resultado = n1 - n2;
                break;
            case 3:
                resultado = n1 * n2;
                break;
            case 4:
                resultado = (float) n1 / n2;
                resultado = Math.round(resultado * 10) / 10.0f;
                break;
            default:
                resultado = 0.00f;
                break;
        }
        if (resultado == (int) resultado) {
            return String.valueOf((int) resultado);
        } else {
            return String.valueOf(resultado).replace(".", ",");
        }
    }

    // O "Maestro" da rodada: Junta todos os métodos de gerar contas, cria duas
    // alternativas com erros matemáticos inteligentes e embaralha as posições dos botões.
    public void posicaoAlternativa() {
        aleatorio_numeros();
        apresentacaoOperacao();
        respostaCorreta();
        String respostaFormatada = respostaCorreta();
        respostaCertaGlobal = respostaFormatada;
        float erro1 = 0f;
        float erro2 = 0f;
        switch (operacao) {
            case 1:
                variacaoDezena = rand.nextBoolean() ? 10 : -10;
                erro1 = resultado + variacaoDezena;
                variacaoUnidade = (rand.nextInt(4) + 1);
                if (rand.nextBoolean()) {
                    erro2 = resultado + variacaoUnidade;
                } else {
                    erro2 = resultado - variacaoUnidade;
                }
                break;
            case 2:
                variacaoDezena = rand.nextBoolean() ? 10 : -10;
                erro1 = resultado + variacaoDezena;
                variacaoUnidade = (rand.nextInt(4) + 1);
                if (rand.nextBoolean()) {
                    erro2 = resultado + variacaoUnidade;
                } else {
                    erro2 = resultado - variacaoUnidade;
                }
                break;
            case 3:
                erro1 = resultado + (rand.nextBoolean() ? n1 : -n1);
                erro2 = resultado + (rand.nextBoolean() ? n2 : -n2);
                break;
            case 4:
                float erroDiv1 = (rand.nextInt(5) + 1) / 10.0f;
                float erroDiv2 = (rand.nextInt(5) + 1) / 10.0f;
                erro1 = resultado + (rand.nextBoolean() ? erroDiv1 : -erroDiv1);
                erro2 = resultado + (rand.nextBoolean() ? erroDiv2 : -erroDiv2);
                erro1 = Math.round(erro1 * 10) / 10.0f;
                erro2 = Math.round(erro2 * 10) / 10.0f;
                break;
        }
        if (erro1 == resultado) {
            erro1 += 2;
        }
        if (erro2 == resultado || erro2 == erro1) {
            erro2 -= 2;
        }
        String textoErro1;
        String textoErro2;
        if (erro1 == (int) erro1) {
            textoErro1 = String.valueOf((int) erro1);
        } else {
            textoErro1 = String.valueOf(erro1).replace(".", ",");
        }
        if (erro2 == (int) erro2) {
            textoErro2 = String.valueOf((int) erro2);
        } else {
            textoErro2 = String.valueOf(erro2).replace(".", ",");
        }
        switch (alternativas) {
            case 1:
                btResposta1.setText(respostaFormatada);
                btResposta2.setText(String.valueOf(textoErro1));
                btResposta3.setText(String.valueOf(textoErro2));
                break;
            case 2:
                btResposta1.setText(String.valueOf(textoErro2));
                btResposta2.setText(respostaFormatada);
                btResposta3.setText(String.valueOf(textoErro1));
                break;
            case 3:
                btResposta1.setText(String.valueOf(textoErro1));
                btResposta2.setText(String.valueOf(textoErro2));
                btResposta3.setText(respostaFormatada);
                break;
        }
    }

    // Verifica se o texto do botão clicado é igual à resposta certa.
    // Se for, soma ponto e avança a rodada. Se não for, decreta o Game Over.
    public void checaAcerto(String textoBotao) {
        if (textoBotao.equals(respostaCertaGlobal)) {
            JOptionPane.showMessageDialog(this, "Acertou");
            pontos += 1;
            lblPontos.setText(String.valueOf(pontos));

        } else {
            gameOver("VOCê ESCOLHEU A RESPOSTA ERRADA! A CERT ERA:" + respostaCertaGlobal);

        }

        posicaoAlternativa();
        iniciarCronometro();
    }

    // Cria e controla o relógio da rodada (15s no Fácil, 10s nos outros níveis).
    // Se o tempo esgotar, para o relógio sozinho e chama o Game Over.
    public void iniciarCronometro() {
        if (nivel.equals("Fácil")) {
            tempoRestante = 15;
        } else {
            tempoRestante = 10;
        }
        lblcont.setText(String.valueOf(tempoRestante));
        if (cronometro == null) {
            cronometro = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    tempoRestante--;
                    lblcont.setText(String.valueOf(tempoRestante));

                    if (tempoRestante <= 0) {
                        cronometro.stop();
                        gameOver("O TEMPO ACABOU, VOCÊ DEMOROU MUITO!");
                    }
                }
            });
        }
        cronometro.restart();
    }

    // Finaliza a partida com segurança: garante que o relógio parou, avisa o 
    // motivo da derrota, chama o salvamento no banco de dados e volta ao Menu Inicial.
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

    // Conecta de forma segura com o banco de dados e atualiza a pontuação
    // do jogador atual baseado no ID que foi gerado na tela de cadastro.
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

    @Override
    public void dispose() {
        if (cronometro != null) {
            cronometro.stop();
            cronometro = null;
        }
        super.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblDif = new javax.swing.JLabel();
        lblNivel = new javax.swing.JLabel();
        lblPtos = new javax.swing.JLabel();
        lblPontos = new javax.swing.JLabel();
        painelPerguntas = new javax.swing.JPanel();
        lblN1 = new javax.swing.JLabel();
        lblSinal = new javax.swing.JLabel();
        lblN2 = new javax.swing.JLabel();
        lblInterrogacao = new javax.swing.JLabel();
        btResposta1 = new javax.swing.JButton();
        btResposta2 = new javax.swing.JButton();
        btResposta3 = new javax.swing.JButton();
        lblcont = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDif.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblDif.setForeground(new java.awt.Color(255, 255, 255));
        lblDif.setText("Dificuldade:");
        getContentPane().add(lblDif, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 18, -1, -1));

        lblNivel.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblNivel.setForeground(new java.awt.Color(255, 255, 255));
        lblNivel.setText("-");
        getContentPane().add(lblNivel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 110, -1));

        lblPtos.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblPtos.setForeground(new java.awt.Color(255, 255, 255));
        lblPtos.setText("Pontos:");
        getContentPane().add(lblPtos, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));

        lblPontos.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblPontos.setForeground(new java.awt.Color(255, 255, 255));
        lblPontos.setText("-");
        getContentPane().add(lblPontos, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 80, -1));

        painelPerguntas.setBackground(new java.awt.Color(0, 96, 57));
        painelPerguntas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pergunta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        lblN1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblN1.setForeground(new java.awt.Color(255, 255, 255));
        lblN1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblN1.setText("N1");

        lblSinal.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSinal.setForeground(new java.awt.Color(255, 255, 255));
        lblSinal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSinal.setText("+");

        lblN2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblN2.setForeground(new java.awt.Color(255, 255, 255));
        lblN2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblN2.setText("N2");

        lblInterrogacao.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblInterrogacao.setForeground(new java.awt.Color(255, 255, 255));
        lblInterrogacao.setText("?");

        javax.swing.GroupLayout painelPerguntasLayout = new javax.swing.GroupLayout(painelPerguntas);
        painelPerguntas.setLayout(painelPerguntasLayout);
        painelPerguntasLayout.setHorizontalGroup(
            painelPerguntasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelPerguntasLayout.createSequentialGroup()
                .addContainerGap(306, Short.MAX_VALUE)
                .addComponent(lblN1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSinal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblN2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInterrogacao)
                .addContainerGap())
        );

        painelPerguntasLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblInterrogacao, lblN1, lblN2, lblSinal});

        painelPerguntasLayout.setVerticalGroup(
            painelPerguntasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelPerguntasLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(painelPerguntasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblN1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSinal)
                    .addComponent(lblN2)
                    .addComponent(lblInterrogacao))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        painelPerguntasLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblInterrogacao, lblN1, lblN2, lblSinal});

        getContentPane().add(painelPerguntas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 460, 230));

        btResposta1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        btResposta1.setText("RESPOSTA 1");
        btResposta1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btResposta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResposta1ActionPerformed(evt);
            }
        });
        getContentPane().add(btResposta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 344, 82));

        btResposta2.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        btResposta2.setText("RESPOSTA 2");
        btResposta2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btResposta2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResposta2ActionPerformed(evt);
            }
        });
        getContentPane().add(btResposta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 344, 82));

        btResposta3.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        btResposta3.setText("RESPOSTA 3");
        btResposta3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btResposta3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResposta3ActionPerformed(evt);
            }
        });
        getContentPane().add(btResposta3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 550, 344, 82));

        lblcont.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblcont, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 660, 70, 70));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/duelo_equacoes/img/Bg.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 0, 500, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btResposta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResposta1ActionPerformed
        checaAcerto(btResposta1.getText());
    }//GEN-LAST:event_btResposta1ActionPerformed

    private void btResposta2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResposta2ActionPerformed
        checaAcerto(btResposta2.getText());
    }//GEN-LAST:event_btResposta2ActionPerformed

    private void btResposta3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResposta3ActionPerformed
        checaAcerto(btResposta3.getText());
    }//GEN-LAST:event_btResposta3ActionPerformed

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
            java.util.logging.Logger.getLogger(TelaPerguntas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPerguntas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPerguntas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPerguntas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPerguntas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btResposta1;
    private javax.swing.JButton btResposta2;
    private javax.swing.JButton btResposta3;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblDif;
    private javax.swing.JLabel lblInterrogacao;
    private javax.swing.JLabel lblN1;
    private javax.swing.JLabel lblN2;
    private javax.swing.JLabel lblNivel;
    private javax.swing.JLabel lblPontos;
    private javax.swing.JLabel lblPtos;
    private javax.swing.JLabel lblSinal;
    private javax.swing.JLabel lblcont;
    private javax.swing.JPanel painelPerguntas;
    // End of variables declaration//GEN-END:variables
}
