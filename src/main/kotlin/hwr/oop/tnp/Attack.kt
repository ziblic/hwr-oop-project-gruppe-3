package hwr.oop.tnp;
enum class Attack(val type: Type, val damage: Int, val hitQuote: Int) {
    SCHLAG(Type.NORMAL, 15, 1),
    TROMMEL(Type.NORMAL, 25, 2),
    NORMALRAMME(Type.NORMAL, 35, 1),
    BODENHAMMER(Type.NORMAL, 45, 2),

    FLAMMKRANZ(Type.FIRE, 20, 1),
    FUNKENSCHNUPPE(Type.FIRE, 10, 1),
    LAVAFLUT(Type.FIRE, 40, 2),
    FEUERSCHWUR(Type.FIRE, 30, 1),

    SPRITZER(Type.WATER, 20, 1),
    WASSERFALL(Type.WATER, 35, 2),
    TIEFSEEGRIFF(Type.WATER, 25, 1),
    TSUNAMI(Type.WATER, 50, 2),

    BLATTPISTOLE(Type.LEAF, 20, 1),
    RAUBBLATT(Type.LEAF, 30, 2),
    WURZELSCHUSS(Type.LEAF, 25, 1),
    LAUBSTURM(Type.LEAF, 45, 2),

    SPUKBALL(Type.GHOST, 20, 1),
    SCHATTENKLAUE(Type.GHOST, 30, 2),
    GEISTWELL(Type.GHOST, 25, 1),
    NACHTSCHREI(Type.GHOST, 40, 2),

}
enum class Type(
    val weakness: List<Type> = emptyList(),
    val resistant: List<Type> = emptyList()
){
    NORMAL(weakness = List(2){Type.FIRE; Type.WATER }, resistant = listOf(Type.NORMAL)),
    FIRE(weakness = List(2){Type.WATER; Type.GHOST}, resistant = listOf(Type.FIRE)),
    WATER(weakness = List(2){Type.LEAF; Type.GHOST}, resistant = listOf(Type.WATER)),
    LEAF(weakness = List(2){Type.FIRE; Type.NORMAL}, resistant = listOf(Type.LEAF)),
    GHOST(weakness = List(2){Type.NORMAL; Type.WATER}, resistant = listOf(Type.GHOST)),
}

fun attack(a: Attack, ma: Monster) {
    if (a.type.weakness.contains(ma.type)) {
        ma.health - (a.damage * 2.0)
    }
    else if (a.type.resistant.contains(ma.type)) {
        ma.health - (a.damage * 0.5)
    }
    else{
        ma.health - a.damage
    }
}

fun getAttack(): Array<Attack>{
    return Attack.values();
}