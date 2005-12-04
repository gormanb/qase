
function [] = SampleBotManagerGeneral(botTime, recordFile)

    prepQASE;   % import the QASE library
    botTime = botTime * 10; % botTime specifies the bot's lifetime in seconds (-1 for infinite)

    try
        matLabBot = MatLabGeneralPollingBot('MatLabGeneralPolling','female/athena');
        matLabBot.connect('127.0.0.1',-1,recordFile);

        pos = [];
        entPos = [];
        entDir = [];
        entDirVect = soc.qase.state.vecmath.Vector3f(0,0,0);

        while(botTime >= 0)
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
                    matLabBot.setBotMovement(entDirVect, entDirVect, 200);
                end

                matLabBot.releaseFromMatLab;
                botTime = botTime - 1;
            end

            timeDecremented = 0;
            pause(0.01);
        end

    catch
        disp 'An error occurred. Disconnecting bots...';
    end

    matLabBot.disconnect;
end
