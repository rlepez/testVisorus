package testvisorus

class Producto {
    String codigo
    String descripcion
    Categoria categoria
    Boolean activo

    static hasMany = [codigoBarra:CodigoBarra]
    static belongsTo = [categoria:Categoria]

    static constraints = {
        codigo size: 1..20
        descripcion size: 5..60

    }
    static mapping = {
        id generator: 'increment'
    }
}
