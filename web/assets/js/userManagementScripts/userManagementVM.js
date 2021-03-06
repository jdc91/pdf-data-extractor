

function ViewModel() {
    var self = this;

    //////// in page navigation ////////
    self.showingDiv = ko.observable('createUser');
    self.setShowingDiv = function(data){
        self.showingDiv(data);
        $('.'+data).addClass('active');
        if(data!='company'){$('.company').removeClass('active');};
        if(data!='createUser'){$('.createUser').removeClass('active');};
        if(data!='hiMan'){$('.hiMan').removeClass('active');};
    };


    //////// tree structure ////////
    var nodeModel = function(data){
        this.id = ko.observable(data.id);
        this.parent = ko.observable(data.parent);
        this.text = ko.observable(data.text);
    };
    var rootNode = {'id':'#', 'parent':undefined, 'text':'root'};
    self.allTreeNodesCollection = ko.observableArray([]);
    self.currentSelectedTreeNode = ko.observable(new nodeModel(rootNode));
    self.currentNodeParent = ko.observable(new nodeModel(rootNode));
    self.isSelectedTemplate = ko.observable(false);
    self.selectedPdfTemplate = ko.observable('');
    self.setRootAsCurrentSelectedTreeNode = function(){
        self.currentSelectedTreeNode(new nodeModel(rootNode));
        self.currentNodeParent(new nodeModel(rootNode));
        selectedNodeRow = undefined;
        selectedNodeChildRow = undefined;
        selectedNodeParentRow = undefined;
        $('#treeViewDiv').jstree("deselect_all");
    };
    self.setCurrentSelectedTreeNode = function(node){
        self.currentSelectedTreeNode(new nodeModel({'id':node.id, 'parent':node.parent, 'text':node.text}));
    };
    self.setIsSelectedTemplate = function(){
        if(selectedNodeRow.original.pdfFile != undefined){
            self.isSelectedTemplate(true);
            self.selectedPdfTemplate(selectedNodeRow.original.pdfFile);
        }else{
            self.isSelectedTemplate(false);
            self.selectedPdfTemplate('');
        }
    };


    /////// user ////////
    function userModel(data){
        var user = this;
        user.username = ko.observable();
        user.fullname = ko.observable();
        user.password = ko.observable();
        user.role = ko.observable();
        if(data!=undefined){
            user.username(data.username);
            user.fullname(data.fullname);
            user.password(data.password);
            user.role(data.role);
        }
    };
    self.newUserBuffer = ko.observable(new userModel());
    self.isUsernameValid = ko.observable(false);
    self.usersCollection = ko.observableArray([]);
    self.selectedUserInHierachyMan = ko.observable();
    var dummy = {username:'u', fullname:'f', password:'p', role:'r'};
    self.selectedUserInHierachyManCopy = ko.observable(dummy);

    self.getAllUsers = function(){
        var userCollection;
        $.ajax({
            type: 'POST', url: 'ManageUsersController',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({request:'getAllUsers'}),
            success: function(data, textStatus, jqXHR) {
                userCollection =  JSON.parse(jqXHR.responseText);
                for(user in userCollection){
                    self.usersCollection.push(new User(userCollection[user]));
                };
            }
        });

    };

    self.createNewUser = function(){
        var sendingDataObj = {
            request: 'createUser',
            userName: self.newUserBuffer().username(),
            fullName: self.newUserBuffer().fullname(),
            pass: self.newUserBuffer().password(),
            role: self.newUserBuffer().role()
        };
        $.ajax({
            type: 'POST', url: 'ManageUsersController',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(sendingDataObj),
            success: function(data, textStatus, jqXHR) {
                var response =  JSON.parse(jqXHR.responseText);
                if(response.state === 'success'){
                    window.location.reload();
                }
                else{
                    alert("couldn't create user.\nPlease try again.");
                }
            }
        });

    };

    self.validateUserName = function(){
        self.isUsernameValid(true);
        for(user in self.usersCollection()){
            if(self.newUserBuffer().username() === self.usersCollection()[user].username){
                self.isUsernameValid(false);
            };
        };
    };

    self.removeUser = function(){
        if(self.selectedUserInHierachyManCopy().username === 'admin'){
            alert('cannot remove admin');
            return false;
        };
        var sendingDataObj = {
            request: 'removeUser',
            id: self.selectedUserInHierachyManCopy().id
        };
        $.ajax({
            type: 'POST', url: 'ManageUsersController',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(sendingDataObj),
            success: function(data, textStatus, jqXHR) {
                var response =  JSON.parse(jqXHR.responseText);
                if(response.state === 'success'){
                    window.location.reload();
                }
                else{
                    alert("couldn't remove user.\nPlease try again.");
                }
            }
        });

    };

    self.setSelectedUserValues = function(){
        self.selectedUserInHierachyManCopy(ko.toJS(self.selectedUserInHierachyMan()));
    };

    self.assignUserToCategory = function(){
        var data = {
            request: "addUserToNode",
            userId: self.selectedUserInHierachyManCopy().id,
            id: self.currentSelectedTreeNode().id(),
            parent: self.currentSelectedTreeNode().parent()
        };
        $.ajax({
            type: 'POST', url: 'AccessRightController',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(data),
            success: function(data, textStatus, jqXHR) {
                var response =  JSON.parse(jqXHR.responseText);
                if(response.state === 'success'){
                    window.location.reload();
                }
                else{
                    alert("couldn't assign user.\nPlease try again.");
                }
            }
        });
    };

    self.removeUserFromNode = function(node){
        var data = {
            request: "removeUserFromNode",
            userId: self.selectedUserInHierachyManCopy().id,
            id: node.id,
            parent: node.parent
        };
        $.ajax({
            type: 'POST', url: 'AccessRightController',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(data),
            success: function(data, textStatus, jqXHR) {
                var response =  JSON.parse(jqXHR.responseText);
                if(response.state === 'success'){
                    window.location.reload();
                }
                else{
                    alert("couldn't remove category.\nPlease try again.");
                }
            }
        });
    };

}

userVM = new ViewModel();

ko.applyBindings(userVM);