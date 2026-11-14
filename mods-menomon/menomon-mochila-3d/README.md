# menomon-mochila-3d 1.1.0 (só cliente)

A mochila do **Inmis** no inventário (e na mão, no chão e nas molduras) passa a ter o mesmo visual 3D e as
mesmas cores que ela tem nas costas do jogador, o modelo do Sophisticated Backpacks usado no Cobbleverse.

**O que o mod faz**
- Modelos de item (`assets/inmis/models/item/*_backpack.json`) para as 8 mochilas (baby, frayed, blazing, plated,
  gilded, bejeweled, withered, endless) apontam para `menomon_mochila3d:item/backpack_base`, com pano, borda e fivelas
  do nível equivalente (couro, cobre, ferro, ouro, diamante, netherite).
- `MochilaCores` registra o tingimento do item: o pano usa a cor do tingimento (ou a cor padrão do SB, a mesma das
  costas) e a borda usa o acento padrão do SB. Assim a mochila tingida no inventário é igual à do corpo.

Funciona junto com o `menomon-mochila-fix` (que prende a mochila ao corpo do jogador); um não depende do outro.
Licença: GPL-3.0 (modelo e texturas do Sophisticated Backpacks, P3pp3rF1y).

**Compilar:** `./build.sh` (javac sobre o jar intermediary, sem Loom). O script espera `libs/fabric-rendering-v1-0.116.12.jar`,
que não está no repositório: extraia-o de `META-INF/jars/` do `fabric-api-0.116.12+1.21.1.jar`.

Entrou no pack **2.26** (e segue no 2.27).
