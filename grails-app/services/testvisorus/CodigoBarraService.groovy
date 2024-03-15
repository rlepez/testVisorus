package testvisorus

import grails.gorm.transactions.Transactional

@Transactional
class CodigoBarraService {

    def serviceMethod() {

    }
    def create(CodigoBarra codigoBarraInstance) throws Exception{
        if (codigoBarraInstance.save(flush:true)){
            log.info 'Componente:codigoBarra, Metodo: create, completado'
            return codigoBarraInstance
        }
        log.error 'Componente:codigoBarra, Metodo:create, error.'
        throw new Exception()
        return null
    }

    def update(CodigoBarra codigoBarraInstance) throws Exception{
        if(!codigoBarraInstance.hasErrors() && codigoBarraInstance.save(flush: true)){
            log.info 'Componente: codigoBarra, Metodo: update, completado'
            return codigoBarraInstance
        }
        log.error 'Componente:codigoBarra, Metodo update, error'
        throw new Exception()
        return null
    }

    def delete (CodigoBarra codigoBarraInstance)throws Exception{
        if(!codigoBarraInstance.delete(flush: true)){
            log.info('Componente:codigoBarra, Metodo: delete. completado ')
            return true
        }
        log.error 'Componente:codigoBarra, Metodo delete, error'
        throw new Exception()
        return null
    }
    def get(Long codigoBarraId){
        log.info 'componente:codigoBarra, Metodo: get, completado'
        return CodigoBarra.get(codigoBarraId)

    }
    def findByCodigoBarra(String codigoBarra){
        log.info 'componente:codigoBarra, Metodo: findByCodigoBarra, completado'
        return findByCodigoBarra(codigoBarra)
    }
}
