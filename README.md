# 杀戮尖塔 爱丽丝 Mod

本仓库是一个 [**杀戮尖塔**](https://store.steampowered.com/app/646570/Slay_the_Spire/) 模组，
添加了一名新的可选角色 [**爱丽丝·玛格特罗伊德**](https://zh.moegirl.org.cn/%E7%88%B1%E4%B8%BD%E4%B8%9D%C2%B7%E7%8E%9B%E6%A0%BC%E7%89%B9%E7%BD%97%E4%BE%9D%E5%BE%B7/)。

爱丽丝是居住在魔法之森的魔法使，拥有操控人偶程度的能力。

## 依赖模组
* [ModTheSpire](https://github.com/kiooeht/ModTheSpire) ：杀戮尖塔 Mod 加载器
* [BaseMod](https://github.com/daviscook477/BaseMod) ：一个 API Mod，同时也提供了游戏内调试用命令行
* [StSLib](https://github.com/kiooeht/StSLib) ：一个供其他 Mod 使用的关键词和机制的集合

## 新机制

### 人偶

爱丽丝可以操控 **人偶**，**人偶** 们会帮助爱丽丝进行战斗，提供各种功能。

[//]: # (带有“人偶”词条的卡牌称为 **人偶** 牌。)

每个人偶按照从右至左的顺序，依次承受由左向右的一个敌人的攻击伤害，溢出伤害由爱丽丝承受。 

与原版的 **充能球** 不同，**人偶** 之间可以存在空位。

- **生成**：人偶优先生成在最右侧的空栏位。
  - 如果没有空栏位，则会 **回收** 最左侧 **人偶**，并使其他 **人偶** 向左移动一格。
  - **可指向生成**：自行选择 **生成** 位置。如果选择的位置上已经有 **人偶**，则会 **回收** 原先的 **人偶**。
- **行动**：人偶的主动效果。**行动** 并不会使人偶消失。
- **回收**：移除人偶。与被摧毁不同，**回收** 可以触发一些效果。
  - 除人偶栏位已满或在非空栏位 **可指向生成** 人偶外，部分效果也可以主动 **回收** 人偶。
- **指令**：部分卡牌可以选择 **人偶** 作为目标，并产生一些额外效果。
  - 如果卡牌只有 **指令** 效果，则必须选择 **人偶** 作为目标。

爱丽丝总共有 7 种 **人偶**，每种人偶都有不同的效果。括号内的数值表示人偶的生命值。

（七色的人偶使有七种人偶很合理吧！）

- **上海** (3)
  - **被动**：**生成** 时对随机敌人造成 5 点伤害。**上海人偶** 的伤害可以享受 **力量** 加成。
  - **行动**：对生命值最低的敌人造成 4 点伤害。
- **蓬莱** (2)
  - **被动**：其他人偶的数值效果提升 1 点。不受 **蓬莱人偶** 影响。
  - **行动**：改为使最右侧的非蓬莱 **人偶** **行动** 一次。
- **法兰西** (5)
  - **被动**：可以承受所有来源的非攻击伤害。每当其它人偶被 **摧毁** 时，自动 **行动** 一次。
  - **行动**：获得 4 点格挡。
- **荷兰** (4)
  - **被动**：为爱丽丝提供 1 点 **力量** 与 **敏捷**。
  - **行动**：爱丽丝获得 1 点临时 **力量** 与 **敏捷**。
- **京都** (5)
  - **被动**：使爱丽丝和所有 **人偶** 在回合开始时保留最多 5 点格挡。
  - **行动**：爱丽丝和所有 **人偶** 获得 1 点格挡。
- **伦敦** (1)
  - **被动**：**生成** 和回合开始时对所有敌人造成 3 点伤害；
             被摧毁或 **回收** 后，在最左侧空栏位随机 **生成** 一个其他类型 **人偶**。
  - **行动**：自己和爱丽丝获得 2 点格挡。
- **奥尔良** (1)
  - **被动**：被 **回收** 时，获得 2 点能量。不受 **蓬莱人偶** 影响。
  - **行动**：抽一张牌。

除此之外，还有一些方式可以获得特殊的 **人偶**，它们不会出现在随机池中。

- **莉莉人偶** (1)
  - **被动**：在你的回合开始时，获得 1 点能量，生效 3 次后自动 **回收**。
    - 被 **回收** 时，为爱丽丝恢复 3 点生命。
  - **行动**：选择一张手牌 **升级**。

### 其他关键词

- **发现**：从三张牌中选择一张，将其加入手牌（如无特殊说明）。
- **临时生命**：一种不会在回合开始时减少的格挡。详见 [StSLib](https://github.com/kiooeht/StSLib)。

[//]: # (- **补充**：抽到这张牌时，再抽一张牌。)
[//]: # (- **脱离**：当这张牌未被打出而离开手牌时，触发某些效果。)

## 致谢
  - Thanks to the [Marisa Mod](https://github.com/lf201014/STS_ThMod_MRS), which teaches me how to develop a mod of STS.
