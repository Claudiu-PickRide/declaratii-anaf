var TsController = (function () {
    function TsController($scope, $http) {
        this.decl106 = "{\n        \"tagName\": \"declaratie106\",\n        \"xmlns\": \"mfp:anaf:dgti:d106:declaratie:v1\",\n        \"d_rec\": \"1\",\n        \"an\": \"2015\",\n        \"luna\": \"12\",\n        \"totalPlata_A\":\"1234\",\n        \"cif\": \"34983363\",\n        \"den\":\"str1234\",\n        \"adresa\":\"str1234\",\n        \"telefon\":\"str1234\",\n        \"fax\":\"str1234\",\n        \"email\": \"alex@example.com\",\n        \"nume_declar\":\"str1234\",\n        \"prenume_declar\":\"str1234\",\n        \"functie_declar\": \"str1234\",\n        \"childNodes\": [{\n            \"tagName\": \"dividende\",\n            \"denAct\":\"str1234\",\n            \"cifAct\":\"34983363\",\n            \"cotaAct\":\"3.14\",\n            \"divd\":\"1234\"\n        }]\n    }";
        $scope.vm = this;
        this.$scope = $scope;
        this.$http = $http;
    }
    TsController.prototype.generate = function () {
        var _this = this;
        console.log("Hello");
        this.$http.post('/validate', this.decl106)
            .then(function (response) {
            _this.response = response.data;
            if (_this.response['fileId'] != '') {
                _this.pdf = _this.response['fileId'];
            }
            console.log(_this.response);
        });
    };
    TsController.$inject = [
        '$scope',
        '$http'
    ];
    return TsController;
})();
angular.module('declaratii', [])
    .controller('mainController', TsController);
//# sourceMappingURL=javascript.js.map