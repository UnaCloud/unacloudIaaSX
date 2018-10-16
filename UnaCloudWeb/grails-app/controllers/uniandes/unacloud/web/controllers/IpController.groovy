package uniandes.unacloud.web.controllersimport org.apache.commons.lang.exception.ExceptionUtilsimport uniandes.unacloud.web.domain.IPPool;import uniandes.unacloud.web.domain.Laboratory;import uniandes.unacloud.web.services.IpServiceimport uniandes.unacloud.web.services.LaboratoryService; class IpController extends AbsAdminController {	//-----------------------------------------------------------------	// Properties	//-----------------------------------------------------------------		/**	 * Representation of IP services	 */	IpService ipService			//-----------------------------------------------------------------	// Actions	//-----------------------------------------------------------------		/**	 * Shows list of ips	 */	def list() {		Laboratory lab = Laboratory.get(params.id)		if (lab && params.pool) {			long ipId = Long.parseLong(params.pool)			def ipPool = IPPool.findWhere(id:ipId, laboratory:lab)			[lab: lab, pool : ipPool]		}		else			redirect(uri:"/admin/lab/list", absolute:true)	}		/**	 * Deletes a valid IP in a lab	 *	 * @return	 */	def delete() {		def lab = Laboratory.get(params.id)		if (lab && params.ip && params.pool) {			try {				ipService.delete(lab, Long.parseLong(params.ip))				flash.message = "Your IP address has been removed"				flash.type = "success"				redirect(uri:"/admin/lab/" + lab.id + "/pool/" + params.pool, absolute:true)			} catch (Exception e) {				flash.message = e.message				redirect(uri:"/admin/lab/" + lab.id, absolute:true)				return			}		}		else			redirect(uri:"/admin/lab/list", absolute:true)	}		/**	 * Changes the state of a IP from AVAILABLE to DISABLE and vis	 * @return	 */	def update(){		def lab = Laboratory.get(params.id)		if (lab && params.ip) {			try {				ipService.updateStatus(lab, Long.parseLong(params.ip))				flash.message = "Your IP address has been modified"				flash.type = "success"				redirect(uri:"/admin/lab/" + lab.id + '/pool/' + params.pool, absolute:true)			} catch(Exception e) {				flash.message = e.message				redirect(uri:"/admin/lab/" + lab.id, absolute:true)				return			}		}		else			redirect(uri:"/admin/lab/list", absolute:true)	}		/**	 * Deletes a valid Pool in a lab	 *	 * @return	 */	def poolDelete(){		print "LLEGA"		def lab = Laboratory.get(params.id)		print "Lab "+lab.id+" "+params.pool		if ( lab && params.pool) {			try {				print "INTENTO"				ipService.deletePool(lab, params.pool)				print "Llego"				flash.message = "Your IP address Pool has been removed"				flash.type = "success"				redirect(uri:"/admin/lab/" + lab.id, absolute:true)			} catch(Exception e) {				print "Exception "+ ExceptionUtils.getRootCauseMessage(e)				flash.message = e.message				redirect(uri:"/admin/lab/" + lab.id, absolute:true)				return			}		}		else			redirect(uri:"/admin/lab/list", absolute:true)	}		/**	 * Renders form to create a new IP Pool	 * @return	 */	def createPool() {		def lab = Laboratory.get(params.id)		if (lab)			[lab:lab]		else redirect(uri:"/admin/lab/list", absolute:true)	}		/**	 * Saves new IP Pool in a Lab	 * @return	 */	def savePool(){		def lab = Laboratory.get(params.id)		if (lab) {			if (params.netGateway && params.netMask && params.ipInit && params.ipEnd) {				try {					ipService.createPool(lab, (params.isPrivate != null), params.netGateway, params.netMask, params.ipInit, params.ipEnd);					flash.type = "success"					flash.message = "Your new IP address Pool has been added to lab"					redirect(uri:"/admin/lab/" + lab.id, absolute:true)				} catch(Exception e) {					flash.message = "Error: " + e.message					redirect(uri:"/admin/lab/" + lab.id + "/pool/new", absolute:true)				}			}			else {				flash.message = "All fields are required"				redirect(uri:"/admin/lab/" + lab.id + "/pool/new", absolute:true)			}		}		else			redirect(uri:"/admin/lab/list", absolute:true)	}}