package testvisorus

class Categoria {
    String codigo
    String descripcion
    Boolean activo

    static hasMany = [producto:Producto]

    static constraints = {
        codigo size: 1..10
        descripcion size: 5..40
    }
    static mapping = {
        id generator:'increment'
    }
}
