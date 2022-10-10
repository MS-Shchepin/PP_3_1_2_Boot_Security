//----------------------------------------Entry-Point-Page-Script-Autorun-----

(async function runPage() {
    await showAllUsers();
    await showAllRoles(document.getElementById("rolesInput"));
}());

//----------------------------------------Service-API-Interactions-------------------

const head = {
    "Content-Type": "application/json",
    "Accept": "application/json",
}

async function getAllUsers() {
    return await fetch("api/users");
}

async function getUserById(id) {
    return await fetch("api/users/" + id);
}

async function getAllRoles() {
    return await fetch("api/roles");
}

async function postNewUser(user) {
    return await fetch("api/users", {
        method: "POST",
        body: JSON.stringify(user),
        headers: head,
    });
}

async function putUserChanges(user) {
    return await fetch("api/users", {
        method: "PUT",
        body: JSON.stringify(user),
        headers: head,
    });
}

async function deleteUserById(id) {
    return await fetch("api/users/" + id, {
        method: "DELETE",
        body: id,
        headers: head,
    });
}

//---------------------------------------Main-Page-User-Table-Filling-----------

async function showAllUsers() {
    let usersTbody = document.getElementById("usersTable").lastElementChild;
    removeAllChildren(usersTbody);
    let response = await getAllUsers();

    if (response.ok) {
        addAllUserRecords(usersTbody, await response.json());
    } else {
        alert("Error get users list, HTTP status: " + response.status);
    }
}

function addAllUserRecords(usersTbody, users) {
    for (let user of users) {
        let userRow = mapUserToTableRow(user);
        usersTbody.append(userRow);
    }
}

//-----------------------------------------Role-Options-Filling------------------------

async function showAllRoles(rolesSelect) {
    removeAllChildren(rolesSelect);
    let response = await getAllRoles();

    if (response.ok) {
        addAllRoleRecords(rolesSelect, await response.json());
    } else {
        alert("Error get roles list, HTTP status: " + response.status);
    }
}

function addAllRoleRecords(rolesSelect, roles) {
    for (let role of roles) {
        let roleOption = document.createElement("option");
        roleOption.text = role.name.replace("ROLE_", "");
        roleOption.value = role.id;
        rolesSelect.append(roleOption);
    }
}

//------------------------------------------Add-New-User-----------------------

function addNewUser() {
    let user = mapToNewUser();
    postNewUser(user)
        .then(response => response.json())
        .then(() => {
            flushAddUserForm();
            showAllUsers().then();
            (function switchTabToUserTable() {
                document.getElementById("userEditor").classList.remove("active", "show");
                document.getElementById("editUserButtonTab").classList.remove("active");
                document.getElementById("userTable").classList.add("active", "show");
                document.getElementById("userTableButtonTab").classList.add("active");
            }());
        })
}

// async function addNewUser() {
//     let user = mapToNewUser();
//     let response = await postNewUser(user);
//
//     if (response.ok) {
//         flushAddUserForm();
//         await showAllUsers();
//         (function switchTabToUserTable() {
//             document.getElementById("userEditor").classList.remove("active", "show");
//             document.getElementById("editUserButtonTab").classList.remove("active");
//             document.getElementById("userTable").classList.add("active", "show");
//             document.getElementById("userTableButtonTab").classList.add("active");
//         }());
//     } else {
//         alert("Error add new user, HTTP status: " + response.status);
//     }
// }

//----------------------------------------Edit-User-Filling----------------------

function fillModalUserEditor(id) {
    return () => {
        getUserById(id)
            .then(response => response.json())
            .then(user => {
                document.getElementById("idInputModalEdit").value = user.id;
                document.getElementById("firstNameInputModalEdit").value = user.name;
                document.getElementById("lastNameInputModalEdit").value = user.lastname;
                document.getElementById("ageInputModalEdit").value = user.age;
                document.getElementById("usernameInputModalEdit").value = user.username;
                let rolesSelect = document.getElementById("rolesSelectModalEdit");
                removeAllChildren(rolesSelect);
                showAllRoles(rolesSelect).then(() => {
                    for (let role of user.roles) {
                        for (let option of rolesSelect.options) {
                            if (option.value == role.id) {
                                option.selected = true;
                                break;
                            }
                        }
                    }
                });
                document.getElementById("editButtonModalEdit").onclick = performEditUser();
            });
    };
}

function performEditUser() {
    return () => putUserChanges(mapToEditUser())
        .then(() => {
                showAllUsers().catch(error => alert(error.message));
                flushEditUserForm();
        }, error => alert(error.message));
}
//----------------------------------------Delete-User-Filling--------------------

function fillModalUserRemover(id) {
    return () => {
        getUserById(id)
            .then(response => response.json())
            .then(user => {
                document.getElementById("idInputModalDelete").value = user.id;
                document.getElementById("firstNameInputModalDelete").value = user.name;
                document.getElementById("lastNameInputModalDelete").value = user.lastname;
                document.getElementById("ageInputModalDelete").value = user.age;
                document.getElementById("usernameInputModalDelete").value = user.username;
                let rolesSelect = document.getElementById("rolesSelectModalDelete");
                removeAllChildren(rolesSelect);
                addAllRoleRecords(rolesSelect, user.roles);
                document.getElementById("deleteButtonModalDelete").onclick = performDeleteUserById(user.id);
            });
    };
}

function performDeleteUserById(id) {
    return () => deleteUserById(id)
        .then(() => showAllUsers().catch(error => alert(error.message)),
                error => alert(error.message));
}

//----------------------------------------Utility-Functions---------------------

function removeAllChildren(parentNode) {
    while (parentNode.firstChild) {
        parentNode.removeChild(parentNode.firstChild);
    }
}


function mapToNewUser() {
    return {
        name: document.getElementById("firstNameInput").value,
        lastname: document.getElementById("lastNameInput").value,
        age: document.getElementById("ageInput").value,
        username: document.getElementById("usernameInput").value,
        password: document.getElementById("passwordInput").value,
        roles: mapSelectedOptionsToRoleList(document.getElementById("rolesInput")),
    }
}

function mapToEditUser() {
    return {
        id: document.getElementById("idInputModalEdit").value,
        name: document.getElementById("firstNameInputModalEdit").value,
        lastname: document.getElementById("lastNameInputModalEdit").value,
        age: document.getElementById("ageInputModalEdit").value,
        username: document.getElementById("usernameInputModalEdit").value,
        password: document.getElementById("passwordInputModalEdit").value,
        roles: mapSelectedOptionsToRoleList(document.getElementById("rolesSelectModalEdit")),
    };
}

function mapSelectedOptionsToRoleList(roleSelect) {
    let selectedRoles = roleSelect.selectedOptions;
    let roles = [];
    for (let i = 0; i < selectedRoles.length; i++) {
        roles[i] = {
            "id": selectedRoles[i].value,
            "name": selectedRoles[i].text,
        }
    }
    return roles;
}


function mapUserToTableRow(user) {
    let userRow = document.createElement("tr");

    for (let key in user) {
        if (key === "password") break;
        let userTd = document.createElement("td");
        userTd.innerText = user[key];
        userRow.append(userTd);
    }

    let roleTd = document.createElement("td");
    user.roles.forEach(role => roleTd.innerText += " " + role.name.replace("ROLE_", ""));
    userRow.append(roleTd);

    let editButton = document.createElement("button");
    editButton.innerText = "Edit";
    editButton.classList.add("btn", "btn-info", "btn-sm");
    editButton.dataset.toggle = "modal";
    editButton.dataset.target = "#editUserModal";
    editButton.value = user.id;
    editButton.onclick = fillModalUserEditor(user.id);
    let editTd = document.createElement("td");
    editTd.append(editButton);

    let deleteButton = document.createElement("button");
    deleteButton.innerText = "Delete";
    deleteButton.classList.add("btn", "btn-danger", "btn-sm");
    deleteButton.dataset.toggle = "modal";
    deleteButton.dataset.target = "#deleteUserModal";
    deleteButton.value = user.id;
    deleteButton.onclick = fillModalUserRemover(user.id);
    let deleteTd = document.createElement("td");
    deleteTd.append(deleteButton);

    userRow.append(editTd);
    userRow.append(deleteTd);
    return userRow;
}

function flushAddUserForm() {
    document.getElementById("firstNameInput").value = "";
    document.getElementById("lastNameInput").value = "";
    document.getElementById("ageInput").value = "";
    document.getElementById("usernameInput").value = "";
    document.getElementById("passwordInput").value = "";
    document.getElementById("rolesInput").selectedIndex = -1;
}

function flushEditUserForm() {
    document.getElementById("idInputModalEdit").value = "";
    document.getElementById("firstNameInputModalEdit").value = "";
    document.getElementById("lastNameInputModalEdit").value = "";
    document.getElementById("ageInputModalEdit").value = "";
    document.getElementById("usernameInputModalEdit").value = "";
    document.getElementById("passwordInputModalEdit").value = "";
    document.getElementById("rolesSelectModalEdit").selectedIndex = -1;
}
