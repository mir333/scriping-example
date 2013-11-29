/* ===========================================================================
 * IBA CZ Confidential
 *
 * (c) Copyright IBA CZ 2011 ALL RIGHTS RESERVED
 * The source code for this program is not published or otherwise
 * divested of its trade secrets.
 *
 * =========================================================================== */
package eu.ibacz.scripting.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;
import eu.ibacz.scripting.ScriptingInterface;

import java.util.List;

/**
 * @author Miroslav Ligas <miroslav.ligas@ibacz.eu>
 */
public class ScriptingInterfaceImpl implements ScriptingInterface {

    @Override
    public void reorganizeWiki() throws SystemException, PortalException {
        Long userId = 10196l;
        Long nodeId = 11730l;
        String content = "test";
        String rootTitle = "FrontPage";

        List<WikiPage> pages = WikiPageLocalServiceUtil.getPages(nodeId, -1, -1);
        for (WikiPage page : pages) {
            String title = page.getTitle();

            if (!rootTitle.equals(title) && title.length() > 1 && getParentTitleSize(page) > 1) {

                char firstLetter = title.charAt(0);
                String categoryTitle = String.valueOf(firstLetter).toUpperCase();
                List<WikiPage> category = WikiPageLocalServiceUtil.getPages(nodeId, categoryTitle, 0, 1);
                if (category != null && !category.isEmpty()) {
                    page.setParentTitle(category.get(0).getTitle());
                    WikiPageLocalServiceUtil.updateWikiPage(page);
                } else {
                    ServiceContext context = new ServiceContext();
                    context.setAddGuestPermissions(true);
                    WikiPage wikiPage = WikiPageLocalServiceUtil.addPage(userId, nodeId, categoryTitle, 1, content, null, false, "creole", true, rootTitle, null, context);
                    wikiPage.setStatus(WorkflowConstants.STATUS_APPROVED);
                    WikiPageLocalServiceUtil.updateWikiPage(wikiPage);
                    page.setParentTitle(categoryTitle);
                    WikiPageLocalServiceUtil.updateWikiPage(page);
                }
            }
        }
    }

    private int getParentTitleSize(WikiPage page) {
        int result = 0;
        String parentTitle = page.getParentTitle();

        if (parentTitle != null) {
            result = parentTitle.length();
        }

        return result;
    }

    @Override
    public void addPermissionsToRole() throws SystemException, PortalException {

        Long roleId = 15724l;
        Long nodeId = 11730l;

        Long companyId = CompanyThreadLocal.getCompanyId();
        List<WikiPage> pages = WikiPageLocalServiceUtil.getPages(nodeId, -1, -1);
        for (WikiPage page : pages) {
            ResourcePermissionLocalServiceUtil.setResourcePermissions(
                    companyId,
                    WikiPage.class.getName(),
                    ResourceConstants.SCOPE_INDIVIDUAL,
                    "" + page.getResourcePrimKey(),
                    roleId,
                    new String[]{ActionKeys.PERMISSIONS});
        }
    }

    public String test() {
        return "Hallo world!";
    }
}
