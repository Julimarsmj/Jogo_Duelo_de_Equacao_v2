/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.duelo_equacoes_classes;

/**
 *
 * @author Julimar
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class ControleMusica {
    private Clip clip;

    public void carregarMusica(String caminhoWav) {
        try {
            
            URL url = getClass().getResource(caminhoWav);
            
            if (url != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            } else {
                System.out.println("Música não encontrada no caminho: " + caminhoWav);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar a música: " + e.getMessage());
        }
    }
    
    public void tocar() {
        if (clip != null && !clip.isRunning()) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            clip.start();
        }
    }

    public void pausar() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); 
        }
    }

    public void alternarMusica() {
        if (clip != null) {
            if (clip.isRunning()) {
                pausar();
            } else {
                tocar();
            }
        }
    }

    public boolean isTocando() {
        return clip != null && clip.isRunning();
    }
}
