package com.example.tarea4dg2445.datos;
public class informacion {
    private Integer id;
    private String nombre,url,descripcion;
    public informacion(){
    }
    public informacion(Integer id, String nombre, String url, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.descripcion = descripcion;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
