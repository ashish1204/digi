/**
 * Created by gaudua on 4/1/2016.
 */

(function () {

    'use strict';

    angular.module('digiAssure.deviceSelection')
        .controller('DeviceSelectionCtrl', DeviceSelectionController);

    DeviceSelectionController.$inject = [ '$scope', '$state'];

    function DeviceSelectionController($scope, $state){

        alert('inside DeviceSelectionController');
    };

    //window.screen.lockOrientation('portrait');

})();

