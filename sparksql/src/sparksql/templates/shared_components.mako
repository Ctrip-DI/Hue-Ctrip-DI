
<%!
def is_selected(section, matcher):
  if section == matcher:
    return "active"
  else:
    return ""
%>

<%def name="menubar(section='')">
  <div class="subnav subnav-fixed">
    <div class="container-fluid">
      <ul class="nav nav-pills">
        <li class="${is_selected(section, 'mytab')}"><a href="#">Tab 1</a></li>
        <li class="${is_selected(section, 'mytab2')}"><a href="#">Tab 2</a></li>
      </ul>
    </div>
  </div>
</%def>
