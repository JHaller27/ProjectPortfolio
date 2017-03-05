var app;
var XPThresholds = {
	'Easy':[25, 50, 75, 125, 250, 300, 350, 450, 550, 600, 800, 1000, 1100, 1250, 1400, 1600, 2000, 2100, 2400, 2800],
	'Medium':[50, 100, 150, 250, 500, 600, 750, 900, 1100, 1200, 1600, 200, 2200, 2500, 2800, 3200, 3900, 4200, 4900, 5700],
	'Hard':[75, 150, 225, 375, 750, 900, 1100, 1400, 1600, 1900, 2400, 3000, 3400, 3800, 4300, 4800, 5900, 6300, 7300, 8500],
	'Deadly':[100, 200, 400, 500, 1100, 1400, 1700, 2100, 2400, 2800, 3600, 4500, 5100, 5700, 6400, 7200, 8800, 9500, 10900, 12700]
};
var XPCRTable = {
	"0":10, "1/8":25, "1/4":50, "1/2":100, "1":200,
	"2":450, "3":700, "4":1100, "6":2300, "5":1800,
	"7":2900, "8":3900, "9":5000, "10":5900, "11":7200,
	"12":8400, "13":10000, "14":11500, "15":13000, "16":15000,
	"17":18000, "18":20000, "19":22000, "20":25000, "21":33000,
	"22":41000, "23":50000, "24":62000, "25":75000, "26":90000,
	"27":105000, "28":120000, "29":135000, "30":155000
};

function isInRange (testValue, lowerLimit, upperLimit) {
	return lowerLimit <= testValue && testValue <= upperLimit;
}

function GetMultiplier (numberOfPlayers, numberOfMonsters) {
	var steps = [1, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6];
	var multipliers = [0.5, 1, 1.5, 2, 2.5, 3, 4, 5];
	var step;
	var multiplier;
	
	// Die if there are no monsters or players
	if (numberOfMonsters <= 0 || numberOfPlayers <= 0) {
		return 0;
	}
	
	// Determine step based on number of monsters (assuming average difficulty)
	if (numberOfMonsters < 15) {
		step = steps[numberOfMonsters - 1];
	}
	else {
		step = steps[14];
	}
	
	// Determine adjustment of step based on number of players
	if (numberOfPlayers < 3) {
		step++;
	}
	else if (numberOfPlayers >= 6) {
		step--;
	}
	
	// Calculate multiplier based on step
	multiplier = multipliers[step];
	
	return multiplier;
}

function GetTypeList (monsters) {
	var arr = [];
	
	for (i in monsters) {
		var type = monsters[i].type;
		var typeIndex = -1;
		
		// Check if type object exists in array
		var typeInArray = false;

		for (x in arr) {
			if (arr[x] == type) {
				typeIndex = x;
				typeInArray = true;
				break;
			}
		}
		
		// Add type object to array
		if (!typeInArray) 
			arr.push(type);
	}
	
	return arr;
}

function InitMonsterList (list) {
	var newList = [];

	for (i in list) {
		newList[i] = new MonsterClass(list[i]);
	}
	
	return newList;
}

//----------------------------------------------------------------------------

// Monster constructor
function MonsterClass (baseMonster) {
	for (i in baseMonster) {
		this[i] = baseMonster[i];
	}
	
	this.XP = XPCRTable[this.CR];
	this.GetTypeText = function () {
		return (this.tags == "") ? this.type : this.type + " (" + this.tags + ")";
	}
	this.GetAlignmentAbbrev = function () {
		var alignmentList = this.alignment.split(" ");
		var str = "";
		for (i in alignmentList) {
			str += alignmentList[i][0].toUpperCase();
		}
		
		return str;
	}
	this.GetStringified = function () {
		return JSON.stringify(this);
	}
}

// Region constructor
function RegionClass (aName) {
	this.name = (aName === undefined) ? "" : aName;
	this.monsters = [];
	this.locked = false;
	this.largestId = -1;
	this.encounter = new EncounterClass();
	
	this.AddMonster = function (monsterStringified) {
		if (this.locked) return;
		
		var obj = new MonsterClass(JSON.parse(monsterStringified));
		obj.id = ++this.largestId;
		this.monsters.push(obj);
	}
	
	this.RemoveMonster = function (idToRemove) {
		if (this.locked) return;
		
		this.monsters = this.monsters.filter(function (monster) {
			return monster.id != idToRemove;
		});
	}
	
	this.Clear = function () {
		if (this.locked) return;
		
		this.largestId = -1;
		this.monsters = [];
	}
	
	this.GetName = function () {
		return (this.locked) ? this.name : "Unlocked";
	}
	
	this.Lock = function(cmd) {
		switch (cmd) {
		case 'Lock':
			this.locked = true;
			break;
		case 'Unlock':
			this.locked = false;
			break;
		case 'Toggle':
			this.locked = !this.locked;
			break;
		}
	}
}

// Character constructor
function CharacterClass (pName, cName, aLevel) {
	this.playerName = pName;
	this.characterName = cName;
	this.level = aLevel;
}

// Encounter constructor
function EncounterClass () {
	this.party = [];
	this.monsters = [];
	this.initiative = -1;
	
	this.GetXPSum = function (someMonsters) {
		var array = (someMonsters === undefined) ? this.monsters : someMonsters;
		var sum = 0;
		
		for (i in array) {
			sum += parseInt(array[i].XP);
		}
		
		return sum;
	}

	this.GetXPPerCharacter = function () {
		return (this.party.length == 0) ? 0 : Math.floor(this.GetXPSum() / this.party.length);
	}
	
	this.GetMultiplier = function(numberOfPlayers, numberOfMonsters) {
		var steps = [1, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6];
		var multipliers = [0.5, 1, 1.5, 2, 2.5, 3, 4, 5];
		var step;
		var multiplier;
		
		// Die if there are no monsters or players
		if (numberOfMonsters <= 0 || numberOfPlayers <= 0) {
			return 0;
		}
		
		// Determine step based on number of monsters (assuming average difficulty)
		if (numberOfMonsters < 15) {
			step = steps[numberOfMonsters - 1];
		}
		else {
			step = steps[14];
		}
		
		// Determine adjustment of step based on number of players
		if (numberOfPlayers < 3) {
			step++;
		}
		else if (numberOfPlayers >= 6) {
			step--;
		}
		
		// Calculate multiplier based on step
		multiplier = multipliers[step];
		
		return multiplier;
	}

	this.GetXPThreshold = function(difficulty) {
		var result = 0;
		
		for (i in this.party) {
			var playerLevel = parseInt(this.party[i].level);
			
			result += parseInt(XPThresholds[difficulty][playerLevel - 1]);
		}
		
		return result;
	}
	
	this.GetAdjustedXP = function(aMonster) {
		var array = ObjCopy(this.monsters);
		
		if (aMonster !== undefined) {
			array.push(aMonster);
		}
		
		var sum = this.GetXPSum(array);
		var mult = this.GetMultiplier(this.party.length, array.length);
		
		return sum * mult;
	}
	
	this.GetDifficulty = function() {
		var xp = this.GetAdjustedXP();
		var threshold = {
			"Easy":this.GetXPThreshold("Easy"),
			"Medium":this.GetXPThreshold("Medium"),
			"Hard":this.GetXPThreshold("Hard"),
			"Deadly":this.GetXPThreshold("Deadly")
		};
		
		if (xp == 0)
			return "n/a";
		else if (xp < threshold.Easy)
			return "Very Easy";
		else if (xp >= threshold.Easy && xp < threshold.Medium)
			return "Easy";
		else if (xp >= threshold.Medium && xp < threshold.Hard)
			return "Medium";
		else if (xp >= threshold.Hard && xp < threshold.Deadly)
			return "Hard";
		else
			return "Deadly";
	}
	
	this.AddMonster = function(monster) {
		this.monsters.push(new MonsterClass(monster));
	}
	
	this.RemoveMonster = function(index) {
		this.monsters = PopAt(this.monsters, index);
	}
	
	this.AddCharacter = function (newCharacter) {
		// Check if player already has a character
		for (i in this.party) {
			if (this.party[i].playerName == newCharacter.playerName) {
				var update = confirm("Player " + newCharacter.playerName + " already has a character: " + this.party[i].characterName + ".\n"
					+ "Would you like to update information for " + newCharacter.playerName + ((newCharacter.playerName.slice(-1) == "s") ? "'" : "'s") + " character?");
					
				if (update) {
					this.party[i] = new CharacterClass(newCharacter.playerName, newCharacter.characterName, newCharacter.level);
				}
				
				return;
			}
		}
		
		this.party.push(new CharacterClass(newCharacter.playerName, newCharacter.characterName, newCharacter.level));
	}
	
	this.RemoveCharacter = function (index) {
		this.party = PopAt(this.party, index);
	}
	
	this.Clear = function() {
		this.monsters = [];
	}
}

//----------------------------------------------------------------------------

app = angular.module('encounterApp', []);
app.controller('ctrl', function($scope) {
	
	// Encounter stuff
	//-------------------------------------------------------------------------
	$scope.monsterList = InitMonsterList(monsterList);
	$scope.typeList = GetTypeList($scope.monsterList);
	
	// Filter stuff
	//-------------------------------------------------------------------------
	$scope.search = {
		type:""
	};
	$scope.alignment = {
		"ethics":{
			"lawful":true,
			"neutral":true,
			"chaotic":true
		},
		"morals":{
			"good":true,
			"neutral":true,
			"evil":true
		},
		"allowUnaligned":true
	};

	$scope.masterListView = false;
	$scope.getExpandedStyle = function () {
		var style = {};
		
		style['flex-direction'] = ($scope.masterListView) ? 'row' : 'column';
		
		return style;
	}
	
	$scope.GetTagList = function(type) {
		var arr = [];
		
		// For every monster...
		for (i in $scope.monsterList) {
			
			// If the monster's type matches passed in type...
			if (type == "" || $scope.monsterList[i].type == type) {
				
				// Get the list of the monster's tags...
				var tagList = $scope.monsterList[i].tags.split(", ");
				
				// For each of the monster's tags...
				for (j in tagList) {
					
					// If the tag is not empty and it doesn't already exit in the return array...
					if (tagList[j] != "" && arr.indexOf(tagList[j]) == -1) {
						
						// Then push the tag to the return array
						arr.push(tagList[j]);
					}
				}
			}
		}
		
		return arr;
	}

	// Region stuff
	//-------------------------------------------------------------------------
	$scope.region = new RegionClass();
	
	$scope.PopulatePlayerInput = function (character) {
		$scope.newCharacter = ObjCopy(character);
	}
	
	// Stash stuff
	//-------------------------------------------------------------------------
	$scope.getStashNames = function (view) {
		var names = localStorage.getItem(view + "Names");
		return (names === null) ? [] : JSON.parse(names);
	};
	
	$scope.stashView = "Region";
	$scope.stashIds = {"Region":0, "Party":0, "Encounter":0};
	$scope.stashNames = {
		"Region":$scope.getStashNames("Region"),
		"Party":$scope.getStashNames("Party"),
		"Encounter":$scope.getStashNames("Encounter")
	};
	$scope.saveName = "";
	
	$scope.save = function() {
		var saveJSON = null;
		if ($scope.saveName != "") {
			if ($scope.stashView == "Region" && $scope.region.monsters.length != 0) {
				saveJSON = JSON.stringify($scope.region);
			} else if ($scope.stashView == "Party" && $scope.region.encounter.party.length != 0) {
				saveJSON = JSON.stringify($scope.region.encounter.party);
			} else if ($scope.stashView == "Encounter" && $scope.region.encounter.monsters.length != 0) {
				$scope.region.encounter
				
				saveJSON = JSON.stringify($scope.region);
			}
			
			if (saveJSON != null) {
				localStorage.setItem($scope.stashView + $scope.saveName, saveJSON);
				
				if ($scope.stashNames[$scope.stashView].indexOf($scope.saveName) == -1) {
					$scope.stashNames[$scope.stashView].push($scope.saveName);
					localStorage.setItem($scope.stashView + "Names", JSON.stringify($scope.stashNames[$scope.stashView]));
				}
			}
		}
	};
	
	$scope.load = function(name) {
		if ($scope.stashView == "Region") {
			$scope.region = new RegionClass(name);
			
			var temp = JSON.parse(localStorage.getItem($scope.stashView + name));
			$scope.region.largestId = temp.largestId;
			
			for (i in temp.monsters) {
				$scope.region.AddMonster(JSON.stringify(temp.monsters[i]));
			}
		} else if ($scope.stashView == "Party") {
			$scope.region.encounter.party = [];
			
			var temp = JSON.parse(localStorage.getItem($scope.stashView + name));
			
			for (i in temp) {
				$scope.region.encounter.AddCharacter(temp[i]);
			}
		}
		
		$scope.saveName = name;
	};
	
	$scope.remove = function(name) {
		if (!confirm('Are you sure you want to remove "' + name + '" from ' + $scope.stashView)) {
			return;
		}
		
		localStorage.removeItem($scope.stashView + name);
		
		$scope.stashNames[$scope.stashView] = PopAt($scope.stashNames[$scope.stashView], $scope.stashNames[$scope.stashView].indexOf(name));
		if ($scope.stashNames[$scope.stashView].length == 0) {
			localStorage.removeItem($scope.stashView + "Names");
		}
		else {
			localStorage.setItem($scope.stashView + "Names", JSON.stringify($scope.stashNames[$scope.stashView]));
		}
		
		$scope.saveName = "";
	}
});

app.filter('alignmentFilter', function()
{
	return function(input, alignmentBoxes)
	{
		var filtered = [];
		
		for (i in input)
		{
			if (input[i].alignment == "any" || (input[i].alignment == "unaligned" && alignmentBoxes.allowUnaligned)) {
				filtered.push(input[i]);
			} else {
				var alignmentArray = (input[i].alignment == "true neutral") ? ["neutral","neutral"] : input[i].alignment.split(" ");

				var ethicsCheck = alignmentArray[0] == "any" || alignmentBoxes.ethics[alignmentArray[0]];
				var moralsCheck = alignmentArray[1] == "any" || alignmentBoxes.morals[alignmentArray[1]];
				
				if (ethicsCheck && moralsCheck) {
					filtered.push(input[i]);
				}
			}
		}
		
		return filtered;
	};
});
app.filter('tagFilter', function()
{
	return function(input, tag)
	{
		var filtered = [];

		for (i in input) {
			if (tag == undefined || tag == "" || input[i].tags.indexOf(tag) != -1) {
				filtered.push(input[i]);
			}
		}

		return filtered;
	};
});
app.filter('range', function() {
	return function(input, total) {
		total = parseInt(total);

		for (var i=1; i<=total; i++) {
			input.push(i);
		}

		return input;
	};
});
