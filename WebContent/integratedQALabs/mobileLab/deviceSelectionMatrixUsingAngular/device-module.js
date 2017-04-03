/**
 * Created by gaudua on 4/1/2016.
 */
(function(){
    'use strict';

    angular.module('digiAssure.deviceSelection', ['ngRoute', 'ui.router'])
        .config(['$stateProvider', '$urlRouterProvider', function ($stateProvider,  $urlRouterProvider) {

            $stateProvider
                .state('deviceGlogal', {
                    url: '/deviceGlogal',
                    templateUrl: '../integratedQALabs/mobileLab/deviceSelectionMatrixUsingAngular/deviceSelection.html',
                    controller: 'DeviceSelectionCtrl'
                })
        }]);
})(angular);