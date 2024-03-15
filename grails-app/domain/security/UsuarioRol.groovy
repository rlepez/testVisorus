package security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class UsuarioRol implements Serializable {

	private static final long serialVersionUID = 1

	Usuario user
	Rol role

	@Override
	boolean equals(other) {
		if (other instanceof UsuarioRol) {
			other.userId == user?.id && other.roleId == role?.id
		}
	}

    @Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (user) {
            hashCode = HashCodeHelper.updateHash(hashCode, user.id)
		}
		if (role) {
		    hashCode = HashCodeHelper.updateHash(hashCode, role.id)
		}
		hashCode
	}

	static UsuarioRol get(long userId, long roleId) {
		criteriaFor(userId, roleId).get()
	}

	static boolean exists(long userId, long roleId) {
		criteriaFor(userId, roleId).count()
	}

	private static DetachedCriteria criteriaFor(long userId, long roleId) {
		UsuarioRol.where {
			user == Usuario.load(userId) &&
			role == Rol.load(roleId)
		}
	}

	static UsuarioRol create(Usuario user, Rol role, boolean flush = false) {
		def instance = new UsuarioRol(user: user, role: role)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(Usuario u, Rol r) {
		if (u != null && r != null) {
			UsuarioRol.where { user == u && role == r }.deleteAll()
		}
	}

	static int removeAll(Usuario u) {
		u == null ? 0 : UsuarioRol.where { user == u }.deleteAll() as int
	}

	static int removeAll(Rol r) {
		r == null ? 0 : UsuarioRol.where { role == r }.deleteAll() as int
	}

	static constraints = {
	    user nullable: false
		role nullable: false, validator: { Rol r, UsuarioRol ur ->
			if (ur.user?.id) {
				if (UsuarioRol.exists(ur.user.id, r.id)) {
				    return ['userRole.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['user', 'role']
		version false
	}
}
