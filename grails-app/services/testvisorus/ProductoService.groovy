package testvisorus

import grails.gorm.transactions.Transactional

@Transactional
class ProductoService {

    def serviceMethod() {

    }
    def create(Producto productoInstance) throws Exception{
        if (productoInstance.save(flush:true)){
            log.info 'Componente:producto, Metodo: create, completado'
            return productoInstance
        }
        log.error 'Componente:producto, Metodo:create, error.'
        throw new Exception()
        return null
    }

    def update(Producto productoInstance) throws Exception{
        if(!productoInstance.hasErrors() && productoInstance.save(flush: true)){
            log.info 'Componente: producto, Metodo: update, completado'
            return productoInstance
        }
        log.error 'Componente:producto, Metodo update, error'
        throw new Exception()
        return null
    }

    def delete (Producto productoInstance)throws Exception{
        if(!productoInstance.delete(flush: true)){
            log.info('Componente:producto, Metodo: delete. completado ')
            return true
        }
        log.error 'Componente:producto, Metodo delete, error'
        throw new Exception()
        return null
    }
    def get(Long productoId){
        log.info 'componente:producto, Metodo: get, completado'
        return Producto.get(productoId)

    }
    def findByCodigo(String codigo){
        return Producto.findByCodigo(codigo)
    }
    def findByDescripcion(String descripcion){
        return Producto.findByDescripcion(descripcion)
    }
    def findByLikeDescripcion(String descripcion){
        return Producto.findAllByDescripcionLike(descripcion)
    }
}
