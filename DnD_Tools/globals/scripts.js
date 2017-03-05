function initFunctions(initFunctions) {
	for(index in initFunctions)
	{
		initFunctions[index]();
	}
}
function ObjCopy (original) {
	return JSON.parse(JSON.stringify(original));
}
function PopAt (arr, index) {
	return (index == -1) ? arr : arr.slice(0, index).concat(arr.slice(index + 1));
}

