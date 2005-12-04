
function [] = BotManagerGeneralTemplate(botTime, recordFile)

    prepQASE;   % import the QASE library
    botTime = botTime * 10; % botTime specifies the bot's lifetime in seconds (-1 for infinite)

    try
        % create and connect the bot - usually one of the MatLabGeneralBots
        matLabBot = MatLabGeneralObserverBot('MatLabGeneralObserver','female/athena');
        matLabBot.connect('127.0.0.1',-1,recordFile);

        % loop for the specified amount of time
        while(botTime >= 0)
            if(matLabBot.waitingForMatLab == 1)

                % World state read from agent %
                % app-dependent computations here %
                % fov, velocity, etc applied directly %

                matLabBot.releaseFromMatLab;
                botTime = botTime - 1;
            end

            pause(0.01);
        end
    catch
        disp 'An error occurred. Disconnecting bots...';
    end

    matLabBot.disconnect;
end
