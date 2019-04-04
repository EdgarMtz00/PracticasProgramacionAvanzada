public class PersonaPojo {
    private String nombre, apellidoPaterno, apellidoMaterno;
    private int edad;

    public PersonaPojo(String nombre, String apellidoPaterno, String apellidoMaterno, int edad) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
    }

    public PersonaPojo() {
        nombre = "sinNombre";
        apellidoPaterno = "sinApellido";
        apellidoMaterno = "sinApellido";
        edad =  0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad){
        if(edad > 150){
            throw new MiErrorrException("No tienes mas de 150, puto");
        }else if(edad < 0){
            throw new MiErrorrException("No mientas >:c");
        }else {
            this.edad = edad;
        }
    }
}
