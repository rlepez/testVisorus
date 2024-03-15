package testvisorus

class CodigoBarra {
    String codigo
    Boolean activo
    static belongsTo = [producto:Producto]

    static constraints = {
        codigo size: 1..20
    }
    static mapping = {
        id generator:'increment'
    }
}
