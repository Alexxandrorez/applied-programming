package org.example.Droids;

public abstract class Droid {
    private final String name;
    private int health;
    private final int maxHealth;
    private final int damage;
    private final String typeName;

    public Droid(String name, int health, int damage, String typeName) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.typeName = typeName;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public String getTypeName() { return typeName; }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public boolean isAlive() { return health > 0; }

    public void restore() { this.health = maxHealth; }

    public void takeDamage(int damage) {
        if (damage > 0) {
            this.health -= damage;
        }

        if (this.health < 0) {
            this.health = 0;
        }
    }
    @Override
    public String toString() {
        return String.format("[%s] (%s) ❤️ %d/%d ⚔️ %d",
                name, typeName, health, maxHealth, damage);
    }
}
