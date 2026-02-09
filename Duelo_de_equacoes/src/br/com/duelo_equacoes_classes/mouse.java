/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.duelo_equacoes_classes;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 *
 * @author Julimar
 */
public class mouse {

    public static void corMouse(JButton botao, Color corNormal, Color corHover) {
        botao.setBackground(corNormal);
        botao.setContentAreaFilled(false);
        botao.setOpaque(true);
        botao.setBorderPainted(false);
        botao.setForeground(Color.white);

        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(corHover);
                botao.setForeground(Color.BLACK);
            }

            public void mouseExited(MouseEvent e) {
                botao.setBackground(corNormal);
                botao.setForeground(Color.white);
            }
        });

    }
}
