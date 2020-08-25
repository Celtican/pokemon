package com.celtican.pokemon.utils.data;

public class Vector2Int {

    public int x;
    public int y;

    public Vector2Int() {}
    public Vector2Int(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Vector2Int(Vector2Int v) {
        x = v.x;
        y = v.y;
    }

    public Vector2Int copy() {
        return new Vector2Int(this);
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }
    public void add(Vector2Int v) {
        add(v.x, v.y);
    }

    public void subtract(int x, int y) {
        this.x -= x;
        this.y -= y;
    }
    public void subtract(Vector2Int v) {
        subtract(v.x, v.y);
    }

    public void multiply(int x, int y) {
        this.x *= x;
        this.y *= y;
    }
    public void multiply(Vector2Int v) {
        multiply(v.x, v.y);
    }

    public void divide(int x, int y) {
        this.x /= x;
        this.y /= y;
    }
    public void divide(Vector2Int v) {
        divide(v.x, v.y);
    }
}
