# 🎮 Projetos de Programação Orientada a Objetos (POO)

Este repositório contém dois jogos desenvolvidos como requisitos avaliativos para a cadeira de **Programação Orientada a Objetos** na **Universidade Estadual do Ceará (UECE)**. Ambos os projetos foram construídos utilizando a linguagem **Java** e a biblioteca gráfica **JavaFX**.

---

## 🎲 1. Jogo de Tabuleiro em Trilha

Um jogo de tabuleiro clássico onde a estratégia e a sorte caminham juntas. O objetivo é atravessar a trilha e chegar ao final antes dos adversários.

### 🛠️ Mecânicas e Funcionalidades
* **Multijogador:** Suporte para múltiplos jogadores por partida.
* **Casas Especiais:** O tabuleiro contém casas de "Sorte ou Azar" que podem conceder vantagens (avançar casas) ou desvantagens (retroceder ou perder o turno).
* **Lógica de Turnos:** Sistema robusto de gerenciamento de rodadas.

### 📸 Galeria de fotos e Gameplay (Tabuleiro)
> **💡 Dica:** Clique na primeira imagem para assistir à demonstração da lógica do jogo!

| [![Vídeo de Gameplay](fotos/1.png)](https://www.youtube.com/watch?v=C_zlEM7erig) |
|:---:|
| 🎬 *ASSISTIR DEMONSTRAÇÃO DO TABULEIRO* |

| 🏫 *Visual do Tabuleiro* | 🏖️ *Evento de Vantagem* | 🏐 *Vencedor* |
|:---:|:---:|:---:|
| ![Tabuleiro](fotos/2.png) | ![Vantagem](fotos/3.png) | ![Vitoria](fotos/9.png) |

---

## 🤖 2. Desafio dos Robôs (Malha Quadriculada)

Um jogo de simulação e controle em uma malha quadriculada, onde o objetivo é levar o robô até o seu "alimento" (objetivo).

### 🕹️ Modos de Jogo
1.  **Modo Manual:** Você assume o controle total do robô e deve traçar o caminho até o objetivo.
2.  **Modo IA (Inteligência Artificial):** O robô é controlado por um algoritmo que busca autonomamente o caminho mais eficiente até o alimento.
3.  **Modo Customizado:** Permite ao usuário posicionar obstáculos na malha para criar novos desafios.

### 📸 Galeria de fotos e Gameplay (Robôs)
| *🌆 Malha Quadriculada* | *🥥 Robô vs Alimento* | *🏐 Obstáculos Customizados* |
|:---:|:---:|:---:|
| ![Malha](fotos/4.png) | ![Objetivo](fotos/5.png) | ![Customizacao](fotos/6.png) |

| *📈 Atributos da IA* | *📚 Lógica do Algoritmo* | *🏁 Simulação* |
|:---:|:---:|:---:|
| ![Status](fotos/7.png) | ![Logica](fotos/8.png) | ![Simulacao](fotos/10.png) |

---

## 📚 Conceitos de POO Aplicados

Em ambos os jogos, foram aplicados os pilares fundamentais da Orientação a Objetos aprendidos na UECE:

* **Encapsulamento:** Proteção dos atributos dos jogadores e robôs.
* **Herança:** Criação de classes base para os tipos de casas do tabuleiro e tipos de robôs.
* **Polimorfismo:** Diferentes comportamentos para o movimento do robô (Manual vs IA) usando os mesmos métodos base.
* **Abstração:** Modelagem das regras de negócio de forma modular e reutilizável.

## 🚀 Como Executar

1.  **Pré-requisitos:** Certifique-se de ter o **JDK 25** (ou superior) instalado e configurado.
2.  **Execução:**
    * Clone o repositório.
    * Abra o projeto no **IntelliJ IDEA**.
    * Execute a classe `Main.java` correspondente ao jogo que deseja testar.

---

### 👨‍💻 Autor
* **Seu Nome** - [Seu GitHub](https://github.com/kaynanxd)
* **Instituição:** Universidade Estadual do Ceará (UECE)

---
*Desenvolvido para fins acadêmicos na cadeira de POO.*
