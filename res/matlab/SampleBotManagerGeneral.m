
% A basic example, showing the operation of the MatLabGeneral bot type. An
% instance of SampleMatLabPollingBot is create and connected. Unlike the
% MatLabBots, no information is preprocessed for MatLab on each update; the
% script must obtain and process all data itself. In this case, the location
% of the nearest item is obtained, and the bot is directed to move towards
% it. This simple example does not employ any pathfinding routines; the
% agent will not take walls, elevation or other obstacles into account.
% This approach provides a direct interface with the agent, but is less
% computationally efficient than the MatLabBot family. For more complex AI
% routines, using Hybrid agents is highly recommended - see sections 5.2 and
% 6.6 of the User's Guide for more on each agent type, and see also the
% SampleBotManager.m script example. MatLabGeneralPollingBot should be used
% in preference to MatLabGeneralObserverBot, due to its better performance;
% for hybrid agents, no such performance discrepancy exists (we typically
% use MatLabObserverBot or MatLabNoClipBot in our own work).

function [] = SampleBotManagerGeneral(botTime, recordFile)

    prepQASE;   % import the QASE library

    try
        matLabBot = MatLabGeneralPollingBot('MatLabGeneralPolling','female/athena');
        matLabBot.connect('127.0.0.1',-1,recordFile);

        pos = [];
        entPos = [];
        entDir = [];
        entDirVect = soc.qase.tools.vecmath.Vector3f(0,0,0);

        tic;

        while(toc < botTime)
            if(matLabBot.waitingForMatLab == 1)
                world = matLabBot.getWorld;

                tempEntity = [];
                nearestEntity = [];
                nearestEntityIndex = -1;
                entDist = 1e10;

                tempOrigin = [];

                player = world.getPlayer;
                entities = world.getItems;
                messages = world.getMessages;

                matLabBot.setAction(0, 0, 0);

                for j = 0 : entities.size - 1
                    tempEntity = entities.elementAt(j);

                    tempOrigin = tempEntity.getOrigin;
                    entPos = [tempOrigin.getX ; tempOrigin.getY];

                    tempOrigin = player.getPlayerMove.getOrigin;
                    pos = [tempOrigin.getX ; tempOrigin.getY];

                    entDir = entPos - pos;

                    if((j == 0 | norm(entDir) < entDist) & norm(entDir) > 0)
                        nearestEntityIndex = j;
                        entDist = norm(entDir);
                    end
                end

                if(nearestEntityIndex ~= -1)
                    nearestEntity = entities.elementAt(nearestEntityIndex);

                    tempOrigin = nearestEntity.getOrigin;
                    entPos = [tempOrigin.getX ; tempOrigin.getY];

                    tempOrigin = player.getPlayerMove.getOrigin;
                    pos = [tempOrigin.getX ; tempOrigin.getY];

                    entDir = entPos - pos;
                    entDir = normc(entDir);

                    entDirVect.set(entDir(1, 1), entDir(2,1), 0);
                    matLabBot.setBotMovement(entDirVect, entDirVect, 200, 0);
                end

                matLabBot.releaseFromMatLab;
            end

            pause(0.01);
        end

    catch
        disp 'An error occurred. Disconnecting bots...';
    end

    matLabBot.disconnect;
end
