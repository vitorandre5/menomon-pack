# menomon-mochila-fix 1.0.0 (só cliente)

Prende a mochila do **Inmis** ao corpo animado do jogador e a desenha com o visual do Cobbleverse.

**Causa:** o `draylar.inmis.client.BackpackFeature` (Inmis 2.8.2) desenha a mochila a partir da pose
base do jogador e imita o agachar com um giro fixo de 25°. Ele nunca aplica a transformação do torso
(`body.rotate`), então não acompanha o Punchy Moves (que anima o `body` e só reaplica o torso para capa
e élitro, em `LegacyTorsoAttachments`), o Emotecraft nem o agachar real.

**O que o mod faz (mixins em `BackpackFeature`, `CapeLayer` e `CustomCapeRenderLayer` do WaveyCapes):**
- Com o Sophisticated Backpacks instalado: desenha a mochila com o modelo de costas do próprio SB
  (`BackpackLayerRenderer.renderBackpack`, que já prende ao torso animado), no nível equivalente
  (baby/frayed=couro, blazing=cobre, plated=ferro, gilded=ouro, bejeweled=diamante, withered/endless=netherite).
  A frayed tingida leva a cor do tingimento.
- Sem o SB: desenha o item 3D do Inmis preso ao torso, sem o giro fixo e 0,9 px mais perto das costas.
- Desenha alças (tiras, peitoral e fivelas) presas ao torso.
- Capa + mochila: igual ao élitro, a capa (vanilla ou WaveyCapes) some e a arte dela vira estampa na mochila.

Compilar: `./build.sh` (javac sobre o jar intermediary, sem Loom, igual aos outros menomon-*).
Relatório e testes: `~/menomon-ports/FIX-MOCHILA.md` na máquina de build.
