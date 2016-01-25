function manifest()
    myManifest = {
        name          = "XSY Faker",
        comment       = "Fake XSY by modifying DYN",
        author        = "kavezo",
        pluginID      = "{F2769fD9-F397-C160-4D04-61F219E05521}", --idk how to get this. Right now, this is just a random hex sequence :I
        pluginVersion = "0.1.0.0",
        apiVersion    = "4.0.0.1"
    }
    
    return myManifest
end


--
--entry point
function main(processParam, envParam)
	local beginPosTick = processParam.beginPosTick -- for some reason all job plugins have this stuff. It's not used later.
	local endPosTick   = processParam.endPosTick
	local songPosTick  = processParam.songPosTick

	local scriptDir  = envParam.scriptDir
	local scriptName = envParam.scriptName
	local tempDir    = envParam.tempDir


	local xsyControl = {}	-- used in making a list of all the XSY control points
	local xsyControlList = {}
	local xsyControlNum

		local defaultXSYControl = {} -- just in case there are no XSY events. We don't want to index error
		defaultXSYControl.posTick = beginPosTick
		defaultXSYControl.value   = 0
		defaultXSYControl.type    = "XSY"
		xsyControlList[1]=defaultXSYControl
	
	local dynControl = {}
	local dynControlList = {}
	local dynControlNum

		local defaultDYNControl = {}
		defaultDYNControl.posTick = beginPosTick
		defaultDYNControl.value   = 64
		defaultDYNControl.type    = "DYN"
		dynControlList[1]=defaultDYNControl
	
	local retCode --"return code". Dummy variable for absorbing error codes.
	local idx --"index"

	local secondary=false --if this is the secondary voice
	
	VSDlgSetDialogTitle("XSY Faker") 						--dialog
	local field = {} 								--make drop-down menu
	field.name       = "xsyPart"
	field.caption    = "Is this part the primary or secondary XSY voice?"
	field.initialVal = "Primary, Secondary"
	field.type = 4
	dlgStatus  = VSDlgAddField(field)
	
	dlgStatus = VSDlgDoModal()

	-- cancel
	if (dlgStatus == 2) then
		return 0
	end

	-- error on weird return codes
	if ((dlgStatus ~= 1) and (dlgStatus ~= 2)) then
		return 1
	end

	-- get choice
	dlgStatus, choice = VSDlgGetStringValue("xsyPart")
	secondary = (choice=="Secondary")
	
	if(secondary) then
		defaultDYNControl.posTick = beginPosTick
		defaultDYNControl.value   = 0
		defaultDYNControl.type    = "DYN"
		dynControlList[1]=defaultDYNControl
	end

	-- populate list of control points in XSY
	retCode = VSSeekToBeginControl("XSY")
	idx = 2
	retCode, xsyControl = VSGetNextControl("XSY")
	while (retCode == 1) do			
		xsyControlList[idx] = xsyControl
		retCode, xsyControl = VSGetNextControl("XSY")
		idx = idx + 1
	end
	xsyControlNum = #xsyControlList
	controlListPrint(xsyControlList)
	
	-- populate list of control points in DYN
	retCode = VSSeekToBeginControl("DYN")
	idx = 2
	retCode, dynControl = VSGetNextControl("DYN")
	while (retCode == 1) do
		dynControlList[idx] = dynControl
		retCode, dynControl = VSGetNextControl("DYN")
		idx = idx + 1
	end
	dynControlNum = #dynControlList
	controlListPrint(dynControlList)

	-- get rid of all existing DYN and XSY points
	for idx = 1, (dynControlNum+xsyControlNum) do
		retCode = VSRemoveControl(xsyControlList[idx])
		retCode = VSRemoveControl(dynControlList[idx])
	end

	-- for every new XSY point, set that as the current multiplier that every new DYN is multiplied by
	local i=1
	local j=1
	local currentDYN=64
	local currentXSY=0
	
	while ((i<=#xsyControlList or j<=#dynControlList) --DO NOT MODIFY THIS CONDITION OR THE OFF-BY-ONE ERRORS WILL HAUNT YOU IN YOUR SLEEP
		and not (i==#xsyControlList+1 and j==#dynControlList+1)) do
	
		local laterTick 		-- the DYN or XSY point currently looked at that occurs later
		local increasei=false		-- because i might be increased before it's referenced, so increase it at the end
		local increasej=false		-- because symmetry
		
		VSMessageBox(i..", " .. j, 0)
		VSMessageBox("Checking condition 125", 0)
		if (j>=#dynControlList+1 or (i<=#xsyControlList and (xsyControlList[i].posTick<=dynControlList[j].posTick))) then		-- we've looked at all the DYN points, or we haven't looked at all the XSY points and the next XSY point is past the next DYN point
			currentXSY=xsyControlList[i].value
			laterTick=xsyControlList[i].posTick
			increasei=true
			VSMessageBox("successful", 0)
		end
		
		VSMessageBox("Checking condition 134", 0)
		if (i>=#xsyControlList+1 or (j<=#dynControlList and (dynControlList[j].posTick<=xsyControlList[i].posTick))) then		-- vice versa
			currentDYN=dynControlList[j].value
			laterTick=dynControlList[j].posTick
			increasej=true
			VSMessageBox("successful", 0)
		end
		VSMessageBox("time: " .. laterTick, 0)
		
		local newValue
		if secondary then
			newValue=currentXSY		-- invert
		else
			newValue=128-currentXSY
		end

		local newControl = {}
		newControl.posTick = laterTick
		newControl.value   = math.floor((newValue/128)*currentDYN)
		newControl.type    = "DYN"
		retCode = VSInsertControl(newControl)
		
		VSMessageBox("new DYN value: " .. (newValue/128)*currentDYN, 0)
		
		if increasei then
			i=i+1
		end
		if increasej then
			j=j+1
		end
		VSMessageBox(i..", " .. j, 0)
	end

	VSMessageBox("Outside loop", 0)
	-- errorless exit
	return 0
end

function controlListPrint(controlList)
	str = ""
	for i=1,#controlList do
		str = str .. "(" .. controlList[i].posTick .. "," .. controlList[i].value .. "); "
	end
	VSMessageBox(str, 0)
end
