/* ===========================================================================
 * IBA CZ Confidential
 *
 * (c) Copyright IBA CZ 2011 ALL RIGHTS RESERVED
 * The source code for this program is not published or otherwise
 * divested of its trade secrets.
 *
 * =========================================================================== */
package eu.ibacz.scripting;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * @author Miroslav Ligas <miroslav.ligas@ibacz.eu>
 */
public interface ScriptingInterface {

    String test();

    void reorganizeWiki() throws SystemException, PortalException;

    void addPermissionsToRole() throws SystemException, PortalException;
}
