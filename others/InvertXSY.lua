--
-- InvertXSY.lua
-- Based off ControlSample3.lua in the Vocaloid3 Job Plugin SDK
-- Inverts the value of XSY.
--

--
-- Copyright (C) 2011 Yamaha Corporation and kávézó
--

function manifest()
    myManifest = {
        name          = "XSY Inverter",
        comment       = "Inverts the value of XSY",
        author        = "kavezo",
        pluginID      = "{98EBB5BE-A7D2-447b-80B8-79603BEA7B79}",
        pluginVersion = "1.0.0.1",
        apiVersion    = "4.0.0.1"
    }
    
    return myManifest
end


--
-- Entry point
--
function main(processParam, envParam)
	-- get times
	local beginPosTick = processParam.beginPosTick
	local endPosTick   = processParam.endPosTick
	local songPosTick  = processParam.songPosTick

	-- get directories
	local scriptDir  = envParam.scriptDir
	local scriptName = envParam.scriptName
	local tempDir    = envParam.tempDir


	local control = {}
	local controlList = {}
	local controlNum
	local retCode
	local idx

	-- the implied 0 control point where there's no beginning control point
	local defaultControl = {}
	defaultControl.posTick = endPosTick
	defaultControl.value   = 0
	defaultControl.type    = "XSY"
	controlList[1]=defaultControl

	-- get control points
	retCode = VSSeekToBeginControl("XSY")
	idx = 1
	retCode, control = VSGetNextControl("XSY")
	while (retCode == 1) do
		controlList[idx] = control
		retCode, control = VSGetNextControl("XSY")
		idx = idx + 1
	end
	controlNum = table.getn(controlList)

	-- erase existing control points
	for idx = 1, controlNum do
		retCode = VSRemoveControl(controlList[idx])
	end

	-- Set each control point to (128 - *their value*), and insert the point
	local pos1 = 1
	while (pos1 < controlNum) do
		control1 = controlList[pos1]
			local newControl = {}
			newControl.posTick = control1.posTick
			newControl.value   = 128-control1.value
			newControl.type    = "XSY"
			retCode = VSInsertControl(newControl)
			pos1 = pos1 + 1
	end


	-- Errorless exit
	return 0
end
