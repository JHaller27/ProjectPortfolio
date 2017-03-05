function EventClass (aName, rep, off) {
	this.name = aName;
	this.repetition = rep;
	this.offset = (off === undefined) ? 0 : off;
	this.editMode = false;
	
	return this;
}

var saveName = "eventmanager";

app = angular.module('eventManagerApp', []);
app.controller('ctrl', function($scope) {
	$scope.day = 1;
	$scope.seeOptions = false;
	
	$scope.events = [];
	$scope.todaysEvents = [];
	
	$scope.saveMode = true;
	
	$scope.AddEvent = function() {
		name = $scope.input.name;
		rep = $scope.input.repetition;
		offset = $scope.input.offset;
		
		if (name != "" && rep != undefined && !$scope.EventExists(name)) {
			$scope.events.push(new EventClass(name, rep, offset));
		}
		
		$scope.UpdateDay();
	}
	
	$scope.RemoveEvent = function(index) {
		$scope.events = PopAt($scope.events, index);
		
		$scope.UpdateDay();
	}
	
	$scope.EventExists = function(aName) {
		for (index in $scope.events) {
			if ($scope.events[index].name == aName) {
				return true;
			}
		}
		
		return false;
	}
	
	$scope.UpdateDay = function(mod) {
		modDay = (mod === undefined) ? $scope.day : $scope.day + mod;
		
		if (modDay > 0) {
			$scope.day = modDay;
			
			$scope.todaysEvents = [];
			for (index in $scope.events) {
				offsetDay = ($scope.day - $scope.events[index].offset);
				if (offsetDay >= 0 && offsetDay % $scope.events[index].repetition == 0) {
					$scope.todaysEvents.push($scope.events[index]);
				}
			}
		}
		
		$scope.Save();
	}
	
	$scope.SetDay = function(newDay) {
		if (newDay === undefined) {
			newDay = prompt('Input new day');
		}
		
		if (!isNaN(parseInt(newDay)) && newDay > 0) {
			$scope.day = parseInt(newDay);
			$scope.UpdateDay();
		}
	}
	
	$scope.Save = function() {
		if (!$scope.saveMode) return;
		
		var saveObj = {
			day: $scope.day,
			seeOptions: $scope.seeOptions,
			events: $scope.events,
			todaysEvents: $scope.todaysEvents
		};
		
		localStorage.setItem(saveName, JSON.stringify(saveObj));
	}
	
	$scope.Load = function() {
		var saveObj = JSON.parse(localStorage.getItem(saveName));
		
		if (saveObj !== null) {
			$scope.day = ObjCopy(saveObj.day);
			$scope.seeOptions = ObjCopy(saveObj.seeOptions);
			$scope.events = ObjCopy(saveObj.events);
			$scope.todaysEvents = ObjCopy(saveObj.todaysEvents);
		}
	}
	
	$scope.Load();
});
