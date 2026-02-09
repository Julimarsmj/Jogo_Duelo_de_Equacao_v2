/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.duelo_equacoes_classes;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Julimar
 */
public class font {

    public Font carregarFonte(float tamanho) {
        try {
            /*
                1. O 'InputStream' é o fluxo de entrada que lê o arquivo binário da fonte. 
            O caminho deve começar com "/" para o Java buscar a partir da raiz do projeto (src).
            */
            InputStream is = getClass().getResourceAsStream("/br/com/duelo_equacoes/fontes/Eraser.ttf");
            /*
            2. Verificação de segurança: se o arquivo não for encontrado no caminho acima, 
            o 'is' será nulo. Lançamos um erro para cair no 'catch' e usar a fonte reserva.
            */
            if (is == null) {
                throw new IOException("Fonte não encontrada!");
            }

            /*
            3. 'createFont' transforma o arquivo físico (.ttf) em um objeto Font do Java.
            O parâmetro 'TRUETYPE_FONT' indica que o formato do arquivo é o padrão TrueType.
            */
            Font fonte = Font.createFont(Font.TRUETYPE_FONT, is);
 
            /*
            4. 'deriveFont' cria uma cópia da fonte carregada, mas com o tamanho específico
            que você passou (ex: 24f). Importante: o parâmetro deve ser float.
            */
            return fonte.deriveFont(tamanho);
        } catch (Exception e) {
            /*
            5. Se qualquer coisa der errado (caminho errado, arquivo corrompido), 
            o programa não trava. Ele retorna a fonte 'Arial' como um plano de segurança.
            */
            System.out.println("ERRO AO CARREGAR FONTE: "+e.getMessage());
            return new Font("Arial", Font.PLAIN, (int) tamanho);
        }
    }

}
