
function [] = BotManagerTemplate(botTime, recordFile)

    prepQASE;   % import the QASE library
    botTime = botTime * 10; % botTime specifies the bot's lifetime in seconds (-1 for infinite)

    % Allocate a cell array to contain MatLab's results. A cell array
    % is used by default because it allows any type of item to be stored and
    % returned - MatLab vectors, scalars, Java objects, etc. However, for maximum
    % efficiency, all inputs and outputs should be passed in the form of simple
    % data types, typically as a series of float[] arrays (see SampleBotManager.m).
    % The column count gives the length of the cell array, which will ultimately
    % be returned to QASE as an Object[] array (again, typically containing one or
    % more float[] arrays for interpretation by the agent's postMatLab method).
    % Simple data types can move between the JVM and MatLab without conversion,
    % while cell arrays are automatically converted to their nearest Java
    % equivalent (Object[]).

    mlResults = cell(1, 1);

    try
        % create and connect the bot - can be either built-in or custom
        matLabBot = SampleMatLabObserverBot('MatLabObserver','female/athena');
        matLabBot.connect('127.0.0.1',-1,recordFile);

        % loop for the specified amount of time
        while(botTime >= 0)
            if(matLabBot.waitingForMatLab == 1)
                mlParams = matLabBot.getMatLabParams;

                % app-dependent computations here %
                % place results in mlResults cell array %

                matLabBot.setMatLabResults(mlResults);

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
