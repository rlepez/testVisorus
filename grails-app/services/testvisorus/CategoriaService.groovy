package testvisorus

import grails.gorm.transactions.Transactional

@Transactional
class CategoriaService {

    def serviceMethod() {

    }
    def create(Categoria categoriaInstance) throws Exception{
        if (categoriaInstance.save(flush:true)){
            log.info 'Componente:Categoria, Metodo: create, completado'
            return categoriaInstance
        }
        log.error 'Componente:Categoria, Metodo:create, error.'
        throw new Exception()
        return null
    }

    def update(Categoria categoriaInstance) throws Exception{
        if(!categoriaInstance.hasErrors() && categoriaInstance.save(flush: true)){
            log.info 'Componente: Categoria, Metodo: update, completado'
            return categoriaInstance
        }
        log.error 'Componente:Categoria, Metodo update, error'
        throw new Exception()
        return null
    }

    def delete (Categoria categoriaInstance)throws Exception{
        if(!categoriaInstance.delete(flush: true)){
            log.info('Componente:Categoria, Metodo: delete. completado ')
            return true
        }
        log.error 'Componente:Categoria, Metodo delete, error'
        throw new Exception()
        return null
    }
    def get(Long categoriaId){
        log.info 'componente:Categoria, Metodo: get, completado'
        return Categoria.get(categoriaId)

    }
    def findByCodigo(String codigo){
        log.info 'componente:Categorioa, Metodo: findByCodigo, completado'
        return Categoria.findByCodigo(codigo)
    }
    def findByDescripcion(String descripcion){
        log.info 'componente:Categoria, Metodo: findBydescripcion, completado'
        return Categoria.findByDescripcion(descripcion)
    }
    def findAllByDescripcionLike(String descripcion){
        log.info 'componente:Categoria, Metodo: findBydescripcionLike, completado'
        return Categoria.findAllByDescripcionLike(descripcion)
    }
    List<Categoria> list(){
        return Categoria.list();
    }

}


