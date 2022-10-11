//----------------------------------------Entry-Point-Page-Script-Autorun-----

(function runScriptOnStart() {
    refreshPage();
}());

function refreshPage() {
    changeInterfaceByRole();
    showAboutUsers();
    showPrincipalHeaderInfo();
}

//----------------------------------------Service-API-Interactions-------------------

function getPrincipal() {
    return fetch("api/principal")
        .then(response => response.json())
        .then(auth => auth.principal);
}

//---------------------------------------Main-Page Info + Header-Filling-------------

function showAboutUsers() {
    getPrincipal()
        .then(principal => {
            document.getElementById("userId").innerText = principal.id;
            document.getElementById("userName").innerText = principal.name;
            document.getElementById("userLastName").innerText = principal.lastname;
            document.getElementById("userAge").innerText = principal.age;
            document.getElementById("userUsername").innerText = principal.username;
            document.getElementById("userRoles").innerText = principal.allRoles;
        });
}

function showPrincipalHeaderInfo() {
    getPrincipal()
        .then(principal => {
            document.getElementById("authUserUsername").innerText = principal.username;
            document.getElementById("authUserRoles").innerText = principal.allRoles;
        });
}

//------------------------------------Interface Changing---------------------------

function changeInterfaceByRole() {
    getPrincipal()
        .then(principal => {
            showAdminInterface(principal.allRoles.includes("ADMIN"));
        });
}

function showAdminInterface(booleanValue) {
    let sidebarAdminButton = document.getElementById("sidebarAdminButton");
        sidebarAdminButton.hidden = !booleanValue;
}