package modelo;

/**
 * Clase modelo Participante.
 * Representa un registro de la tabla "participantes" en la base de datos.
 * Sigue el patrón JavaBean: constructor, getters y setters.
 */
public class Participante {

    private int    id;
    private String cedula;
    private String nombre;
    private String apellido;
    private int    edad;
    private String correo;
    private String estadoCivil;
    private String jornada;
    private String categoria;
    private String observaciones;

    // Constructor vacío (necesario para JavaFX TableView)
    public Participante() {}

    // Constructor con todos los campos (excepto id, que es auto)
    public Participante(String cedula, String nombre, String apellido,
                        int edad, String correo, String estadoCivil,
                        String jornada, String categoria, String observaciones) {
        this.cedula        = cedula;
        this.nombre        = nombre;
        this.apellido      = apellido;
        this.edad          = edad;
        this.correo        = correo;
        this.estadoCivil   = estadoCivil;
        this.jornada       = jornada;
        this.categoria     = categoria;
        this.observaciones = observaciones;
    }

    // Constructor completo (incluye id, usado al leer de BD)
    public Participante(int id, String cedula, String nombre, String apellido,
                        int edad, String correo, String estadoCivil,
                        String jornada, String categoria, String observaciones) {
        this(cedula, nombre, apellido, edad, correo, estadoCivil, jornada, categoria, observaciones);
        this.id = id;
    }

    // ---- Getters ----
    public int    getId()            { return id; }
    public String getCedula()        { return cedula; }
    public String getNombre()        { return nombre; }
    public String getApellido()      { return apellido; }
    public int    getEdad()          { return edad; }
    public String getCorreo()        { return correo; }
    public String getEstadoCivil()   { return estadoCivil; }
    public String getJornada()       { return jornada; }
    public String getCategoria()     { return categoria; }
    public String getObservaciones() { return observaciones; }

    // ---- Setters ----
    public void setId(int id)                       { this.id = id; }
    public void setCedula(String cedula)            { this.cedula = cedula; }
    public void setNombre(String nombre)            { this.nombre = nombre; }
    public void setApellido(String apellido)        { this.apellido = apellido; }
    public void setEdad(int edad)                   { this.edad = edad; }
    public void setCorreo(String correo)            { this.correo = correo; }
    public void setEstadoCivil(String estadoCivil)  { this.estadoCivil = estadoCivil; }
    public void setJornada(String jornada)          { this.jornada = jornada; }
    public void setCategoria(String categoria)      { this.categoria = categoria; }
    public void setObservaciones(String obs)        { this.observaciones = obs; }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (CI: " + cedula + ")";
    }
}
