var Enum = {
	Ability:{
		Strength:0,
		Dexterity:1,
		Constitution:2,
		Intelligence:3,
		Wisdom:4,
		Charisma:5
	}
}

var abilityCostTable = {
	8:0,
	9:1,
	10:2,
	11:3,
	12:4,
	13:5,
	14:7,
	15:9
}

// AbilityClass Constructor
function AbilityClass(aName) {
	this.name = aName;
	this.abilityScore = 8;
	
	this.GetName = function() {
		return this.name;
	};
	
	this.GetAbilityScore = function() {
		return this.abilityScore;
	};
	
	this.GetPointsSpent = function() {
		return abilityCostTable[this.GetAbilityScore()];
	};
	
	this.GetCostToIncrease = function(newScore) {
		if (newScore === undefined) {
			newScore = this.GetAbilityScore() + 1;
		}
		
		return (8 <= newScore && newScore <= 15) ? abilityCostTable[newScore] - abilityCostTable[this.GetAbilityScore()] : "n/a";
	}
	
	this.SetAbilityScore = function(newScore, pointsRemaining) {
		// Loop Control Variable
		var lcv = true;
		
		var incrementSign = Math.sign(newScore - this.GetAbilityScore());
		
		/* Attempt to inc/dec ability score by 1 until either the new ability score
		is reached or until another step would set the ability score out of bounds */
		do {
			var cost = this.GetCostToIncrease(this.GetAbilityScore() + incrementSign);
			lcv = this.IncreaseAbilityScore(incrementSign, pointsRemaining);
			
			if (lcv) pointsRemaining -= cost;
		} while(lcv && this.GetAbilityScore() != newScore);
	}
	
	this.IncreaseAbilityScore = function(amt, pointsRemaining) {
		var newScore = this.GetAbilityScore() + amt;
		var pointsAfterIncrease = pointsRemaining - this.GetCostToIncrease(newScore);
		
		if ((8 <= newScore && newScore <= 15) && (pointsAfterIncrease >= 0)) {
			this.abilityScore = newScore;
			return true;
		} else {
			return false;
		}
	}
}

function RaceClass(aName, statsArrayFunk) {
	this.name = aName;
	this.Strength = 0;
	this.Dexterity = 0;
	this.Constitution = 0;
	this.Intelligence = 0;
	this.Wisdom = 0;
	this.Charisma = 0;
	this.statsArrayFunction = statsArrayFunk;
	
	this.Select = function() {
		var statsArray = this.statsArrayFunction();
		this.Strength = statsArray[0];
		this.Dexterity = statsArray[1];
		this.Constitution = statsArray[2];
		this.Intelligence = statsArray[3];
		this.Wisdom = statsArray[4];
		this.Charisma = statsArray[5];
		
		return this;
	}
	
	this.GetModifier = function(statName) {
		var mod = this[statName];
		return (mod > 0) ? "+" + mod : "" + mod;
	}
}

var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope) {
	$scope.maxPoints = 27;
	$scope.abilities = [
		new AbilityClass('Strength'),
		new AbilityClass('Dexterity'),
		new AbilityClass('Constitution'),
		new AbilityClass('Intelligence'),
		new AbilityClass('Wisdom'),
		new AbilityClass('Charisma')
	];
	$scope.races = [
		new RaceClass('None', function () { return [0,0,0,0,0,0]; }),
		new RaceClass('Dwarf', function () {
			var statArray = [0,0,0,0,0,0];
			statArray[Enum.Ability.Constitution] = 2;
			
			// Determine subrace
			var promptText = "Input subrace\n - Hill: +1 Wisdom\n - Mountain: +2 Strength";
			var subrace = prompt(promptText);
			var validSubrace = false;
			do {
				switch(subrace) {
					case "Hill":
						statArray[Enum.Ability.Wisdom] = 1;
						break;
					case "Mountain":
						statArray[Enum.Ability.Strength] = 2;
						break;
					default:
						validSubrace = false;
				}
				
				if (!validSubrace) {
					subrace = prompt("Invalid input\n" + promptText);
				}
			} while(!validSubrace);
			
			return statArray;
		}),
		new RaceClass('Elf', function () {
			var statArray = [0,0,0,0,0,0];
			statArray[Enum.Ability.Dexterity] = 2;
			
			// Determine subrace
			var promptText = "Input subrace\n - High: +1 Intelligence\n - Wood: +1 Wisdom\n - Drow:+1 Charisma";
			var subrace = prompt(promptText);
			var validSubrace = false;
			do {
				switch(subrace) {
					case "High":
						statArray[Enum.Ability.Intelligence] = 1;
						validSubrace = true;
						break;
					case "Wood":
						statArray[Enum.Ability.Wisdom] = 1;
						validSubrace = true;
						break;
					case "Drow":
						statArray[Enum.Ability.Charisma] = 1;
						validSubrace = true;
						break;
					default:
						validSubrace = false;
				}
				
				if (!validSubrace) {
					subrace = prompt("Invalid input" + promptText);
				}
			} while(!validSubrace);
			
			return statArray;
		}),
		new RaceClass('Halfling', function () {
			var statArray = [0,0,0,0,0,0];
			statArray[Enum.Ability.Dexterity] = 2;
			
			// Determine subrace
			var promptText = "Input subrace\n - Lightfoot: +1 Charisma\n - Stout: +1 Constitution";
			var subrace = prompt(promptText);
			var validSubrace = false;
			do {
				switch(subrace) {
					case "Lightfoot":
						statArray[Enum.Ability.Charisma] = 1;
						validSubrace = true;
						break;
					case "Stout":
						statArray[Enum.Ability.Constitution] = 1;
						validSubrace = true;
						break;
					default:
						validSubrace = false;
				}
				
				if (!validSubrace) {
					subrace = prompt("Invalid input" + promptText);
				}
			} while(!validSubrace);
			
			return statArray;
		}),
		new RaceClass('Human', function () {
			// Determine subrace
			var isVariant = confirm("Would you like to choose the human variant?\n(Select 'OK' to choose 2 +1 stats, select 'Cancel' to choose all +1 stats)");
			
			if (!isVariant) {
				return [1,1,1,1,1,1];
			}
			
			var statArray = [0,0,0,0,0,0];
			
			for (var i = 0; i < 2; i++) {
				// Assign stat names to increase
				var statName = prompt("Input ability " + (i + 1) + " of 2 to increase by 1\n(full name, no abbreviations)");
				var statIndex = -1;
				
				var validStat = false;
				do {
					switch(statName) {
						case "Strength":
						case "Dexterity":
						case "Constitution":
						case "Intelligence":
						case "Wisdom":
						case "Charisma":
							validStat = true;
							break;
						default:
							validStat = false;
					}
					
					if (!validStat) {
						statName = prompt("Name invalid\nInput ability " + (i + 1) + " of 2 to increase by 1\n(full name, no abbreviations)");
					}
				} while(!validStat);
				
				// Set stat to +1
				statArray[Enum.Ability[statName]] = 1;
			}
			
			return statArray;
		}),
		new RaceClass('Dragonborn', function () {
			var statArray = [0,0,0,0,0,0];
			statArray[Enum.Ability.Strength] = 2;
			statArray[Enum.Ability.Charisma] = 1;
			return statArray;
		}),
		new RaceClass('Gnome', function () {
			var statArray = [0,0,0,0,0,0];
			statArray[Enum.Ability.Intelligence] = 2;
			
			// Determine subrace
			var promptText = "Input subrace\n - Forest: +1 Dexterity\n - Rock: +1 Constitution";
			var subrace = prompt(promptText);
			var validSubrace = false;
			do {
				switch(subrace) {
					case "Forest":
						statArray[Enum.Ability.Dexterity] = 1;
						validSubrace = true;
						break;
					case "Rock":
						statArray[Enum.Ability.Constitution] = 1;
						validSubrace = true;
						break;
					default:
						validSubrace = false;
				}
				
				if (!validSubrace) {
					subrace = prompt("Invalid input" + promptText);
				}
			} while(!validSubrace);
			
			return statArray;
		}),
		new RaceClass('Half-Elf', function () {
			var statArray = [0,0,0,0,0,0];
			statArray[Enum.Ability.Charisma] = 2;
			
			for (var i = 0; i < 2; i++) {
				// Assign stat names to increase
				var statName = prompt("Input ability " + (i + 1) + " of 2 to increase by 1\n(full name, no abbreviations)");
				var statIndex = -1;
				
				var validStat = false;
				do {
					switch(statName) {
						case "Strength":
						case "Dexterity":
						case "Constitution":
						case "Intelligence":
						case "Wisdom":
						case "Charisma":
							validStat = true;
							break;
						default:
							validStat = false;
					}
					
					if (!validStat) {
						statName = prompt("Name invalid\nInput ability " + (i + 1) + " of 2 to increase by 1\n(full name, no abbreviations)");
					}
				} while(!validStat);
				
				// Set stat to +1
				statArray[Enum.Ability[statName]] = 1;
			}
			
			return statArray;
		}),
		new RaceClass('Half-Orc', function () {
			var statArray = [0,0,0,0,0,0];
			statArray[Enum.Ability.Strength] = 2;
			statArray[Enum.Ability.Constitution] = 1;
			return statArray;
		}),
		new RaceClass('Tiefling', function () {
			var statArray = [0,0,0,0,0,0];
			statArray[Enum.Ability.Charisma] = 2;
			statArray[Enum.Ability.Intelligence] = 1;
			return statArray;
		})
	];
	$scope.selectedRace = $scope.races[0];
	
	$scope.GetAbilityScore = function(ability) {
		return parseInt(ability.GetAbilityScore()) + parseInt($scope.selectedRace.GetModifier(ability.name));
	};
	
	$scope.GetModifier = function(ability) {
		var abilityScore = $scope.GetAbilityScore(ability);
		var mod = Math.floor(abilityScore / 2) - 5;
		return (mod > 0) ? "+" + mod : "" + mod;
	};
	
	$scope.IncreaseAbilityScore = function(ability, amt) {
		ability.SetAbilityScore(ability.GetAbilityScore() + amt);
	}
	
	$scope.SelectRace = function(race) {
		$scope.selectedRace = race.Select();
	};
	
	$scope.IncreaseMaxPoints = function(amt) {
		if (amt === undefined) {
			amt = 1;
		}
		
		var newMax = $scope.maxPoints + amt;
		
		if (9 <= newMax && newMax <= 54) {
			$scope.maxPoints = newMax;
		}
	}
	
	$scope.GetTotalPointsSpent = function() {
		var total = 0;
		
		for (i in $scope.abilities) {
			total += $scope.abilities[i].GetPointsSpent();
		}
		
		return total;
	};
	
	$scope.GetPointsRemaining = function() {
		return $scope.maxPoints - $scope.GetTotalPointsSpent();
	}
	
	$scope.Clear = function() {
		for (i in $scope.abilities) {
			$scope.abilities[i].SetAbilityScore(8, $scope.GetPointsRemaining());
		}
	}
	
	$scope.$watch('abilities[0].pointsSpent', function(newValue, oldValue) {
		if ($scope.maxPoints - $scope.GetTotalPointsSpent() < 0 || newValue < 0 || newValue > 9) {
			$scope.abilities[0].pointsSpent = oldValue;
		}
	});
	$scope.$watch('abilities[1].pointsSpent', function(newValue, oldValue) {
		if ($scope.maxPoints - $scope.GetTotalPointsSpent() < 0 || newValue < 0 || newValue > 9) {
			$scope.abilities[1].pointsSpent = oldValue;
		}
	});
	$scope.$watch('abilities[2].pointsSpent', function(newValue, oldValue) {
		if ($scope.maxPoints - $scope.GetTotalPointsSpent() < 0 || newValue < 0 || newValue > 9) {
			$scope.abilities[2].pointsSpent = oldValue;
		}
	});
	$scope.$watch('abilities[3].pointsSpent', function(newValue, oldValue) {
		if ($scope.maxPoints - $scope.GetTotalPointsSpent() < 0 || newValue < 0 || newValue > 9) {
			$scope.abilities[3].pointsSpent = oldValue;
		}
	});
	$scope.$watch('abilities[4].pointsSpent', function(newValue, oldValue) {
		if ($scope.maxPoints - $scope.GetTotalPointsSpent() < 0 || newValue < 0 || newValue > 9) {
			$scope.abilities[4].pointsSpent = oldValue;
		}
	});
	$scope.$watch('abilities[5].pointsSpent', function(newValue, oldValue) {
		if ($scope.maxPoints - $scope.GetTotalPointsSpent() < 0 || newValue < 0 || newValue > 9) {
			$scope.abilities[5].pointsSpent = oldValue;
		}
	});
	
});
