
function [] = BotManagerGeneralTemplate(botTime, recordFile) % botTime specifies the bot's lifetime in seconds (-1 for infinite)

    prepQASE;   % import the QASE library

    try
        % create and connect the bot
        matLabBot = MatLabGeneralPollingBot('MatLabGeneralPolling','female/athena');
        matLabBot.connect('127.0.0.1',-1,recordFile);

        tic;

        % loop for the specified amount of time
        while(toc < botTime)
            if(matLabBot.waitingForMatLab == 1)

                % World state read from agent %
                % app-dependent computations here %
                % fov, velocity, etc applied directly %

                matLabBot.releaseFromMatLab;
            end

            pause(0.01);
        end
    catch
        disp 'An error occurred. Disconnecting bots...';
    end

    matLabBot.disconnect;
end
