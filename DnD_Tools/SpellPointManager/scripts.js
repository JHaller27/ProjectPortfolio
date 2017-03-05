var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope)
{
	$scope.classes = [
		{"name":"Bard", "divisor":1, "maxLevel":20},
		{"name":"Cleric", "divisor":1, "maxLevel":20},
		{"name":"Druid", "divisor":1, "maxLevel":20},
		{"name":"Sorcerer", "divisor":1, "maxLevel":20},
		{"name":"Wizard", "divisor":1, "maxLevel":20},
		{"name":"Paladin", "divisor":2, "maxLevel":20},
		{"name":"Ranger", "divisor":2, "maxLevel":20},
		{"name":"Fighter", "divisor":3, "maxLevel":20},
		{"name":"Rogue", "divisor":3, "maxLevel":20}
	];
	$scope.levels = {};
	$scope.maxSpellLevel = 9;
	$scope.spellPointsUsed = 0;
	$scope.range = GetRange($scope.maxSpellLevel);
	
	// Initialize levels array
	for (var index = 0; index < $scope.classes.length; index++)
	{
		var aClass = $scope.classes[index];
		$scope.levels[ aClass["name"] ] = {"level":0, "divisor":aClass["divisor"]};
	}
	
	$scope.slotPerLevelTable =
	[
		[0,0,0,0,0,0,0,0,0],	// 0
		[2,0,0,0,0,0,0,0,0],	// 1
		[3,0,0,0,0,0,0,0,0],	// 2
		[4,2,0,0,0,0,0,0,0],	// 3
		[4,3,0,0,0,0,0,0,0],	// 4
		[4,3,2,0,0,0,0,0,0],	// 5
		[4,3,3,0,0,0,0,0,0],	// 6
		[4,3,3,1,0,0,0,0,0],	// 7
		[4,3,3,2,0,0,0,0,0],	// 8
		[4,3,3,3,1,0,0,0,0],	// 9
		[4,3,3,3,2,0,0,0,0],	// 10
		[4,3,3,3,2,1,0,0,0],	// 11
		[4,3,3,3,2,1,0,0,0],	// 12
		[4,3,3,3,2,1,1,0,0],	// 13
		[4,3,3,3,2,1,1,0,0],	// 14
		[4,3,3,3,2,1,1,1,0],	// 15
		[4,3,3,3,2,1,1,1,0],	// 16
		[4,3,3,3,2,1,1,1,1],	// 17
		[4,3,3,3,3,1,1,1,1],	// 18
		[4,3,3,3,3,2,1,1,1],	// 19
		[4,3,3,3,3,2,2,1,1]		// 20
	];
	
	// index + 1 = spell level
	// element = spell point cost
	$scope.conversion = [2, 3, 5, 6, 7, 9, 10, 11, 13];
	
	// index = spell level
	// element = number of spell slots
	$scope.maxSlots = $scope.slotPerLevelTable[0];
	
	$scope.saveName;
	
	$scope.GetMaxSpellPoints = function ()
	{
		var sum = 0;
		
		for (var i = 0; i < $scope.maxSpellLevel; i++)
		{
			sum += $scope.conversion[i] * $scope.maxSlots[i];
		}
		
		return sum;
	};
	
	$scope.GetSpellPoints = function ()
	{
		return $scope.GetMaxSpellPoints() - $scope.spellPointsUsed;
	}
	
	$scope.GetOrdinal = function (num)
	{
		var ordinal = "" + num;
		num = parseInt(num);
		
		switch(num % 10)
		{
		case 1:
			ordinal += "st";
			break;
		case 2:
			ordinal += "nd";
			break;
		case 3:
			ordinal += "rd";
			break;
		default:
			ordinal += "th";
		}
		
		return ordinal;
	}
	
	$scope.GetCasterLevel = function ()
	{
		var sum = 0;
		
		for (var i = 0; i < $scope.classes.length; i++)
		{
			var className = $scope.classes[i]["name"];
			var levelObj = $scope.levels[className];
			sum += Math.floor(levelObj["level"] / levelObj["divisor"]);
		}
		
		return sum;
	}
	
	$scope.GetCharacterLevel = function ()
	{
		var sum = 0;
		
		for (var i = 0; i < $scope.classes.length; i++)
		{
			sum += $scope.levels[$scope.classes[i]["name"]]["level"];
		}
		
		return sum;
	}
	
	$scope.AddLevel = function (casterType)
	{
		var casterTypeLevel = $scope.levels[ casterType['name'] ]['level'];
		
		if (casterTypeLevel < casterType['maxLevel'])
		{
			$scope.levels[ casterType['name'] ]['level']++;
			
			var casterLevel = $scope.GetCasterLevel();
			
			if (casterLevel < 0)
			{
				$scope.maxSlots = $scope.slotPerLevelTable[0];
			}
			else
			{
				$scope.maxSlots = GetArray($scope.slotPerLevelTable[casterLevel]);
			}
		}
	}
	
	$scope.SubtractLevel = function (casterType)
	{
		var casterTypeLevel = $scope.levels[ casterType['name'] ]['level'];
		
		if (casterTypeLevel > 0)
		{
			$scope.levels[casterType['name']]['level']--;
			
			var casterLevel = $scope.GetCasterLevel();
			
			if (casterLevel < 0 || $scope.GetCharacterLevel() <= 0)
			{
				$scope.maxSlots = $scope.slotPerLevelTable[0];
			}
			else
			{
				$scope.maxSlots = GetArray($scope.slotPerLevelTable[casterLevel]);
			}
		}
	}
	
	$scope.ResetLevels = function (casterType)
	{
		if (casterType !== undefined)
		{
			$scope.levels[ casterType['name'] ]['level'] = 0;
		}
		else
		{
			for (var i = 0; i < $scope.classes.length; i++)
			{
				casterType = $scope.classes[i];
				
				$scope.levels[ casterType['name'] ]['level'] = 0;
			}
		}
		
		var casterLevel = $scope.GetCasterLevel();
		
		if (casterLevel < 0 || $scope.GetCharacterLevel() <= 0)
		{
			$scope.maxSlots = $scope.slotPerLevelTable[0];
		}
		else
		{
			$scope.maxSlots = GetArray($scope.slotPerLevelTable[casterLevel]);
		}
	}
	
	$scope.CastSpell = function (spellLevel)
	{
		var spellPointsCast = $scope.conversion[spellLevel - 1];
		
		if ( ($scope.GetSpellPoints() - spellPointsCast) >= 0 )
		{
			$scope.spellPointsUsed += spellPointsCast;
		}
	}
	
	$scope.RegainSpell = function (spellLevel)
	{
		var spellPointsCast = $scope.conversion[spellLevel - 1];
		
		if ( ($scope.GetSpellPoints() + spellPointsCast) < $scope.GetMaxSpellPoints() )
		{
			$scope.spellPointsUsed -= spellPointsCast;
		}
	}
	
	$scope.ResetSlots = function ()
	{
		$scope.spellPointsUsed = 0;
	}
	
	$scope.Save = function ()
	{
		var storageText = "";
		
		
	}
});

function GetRange(max)
{
	var range = [];
	
	for (var i = 0; i < max; i++)
	{
		range[i] = i + 1;
	}
	
	return range;
}

function GetArray(arrIn)
{
	var arrOut = [];
	
	for (var i = 0; i < arrIn.length; i++)
	{
		arrOut.push(arrIn[i]);
	}
	
	return arrOut;
}