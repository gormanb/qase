
% A basic example, showing the interaction of QASE and MatLab. An instance
% of SampleMatLabObserverBot is create and connected. On each update, it
% provides the position of the agent and that of the nearest item, each
% in the form of a float array. MatLab then computes the direction the
% agent needs to move, and passes back a float vector (which is auto-
% converted into a Java float[] array). This simple example does not employ
% any pathfinding routines; the agent will not take walls, elevation or
% other obstacles into account. The most important aspect of this example
% is that MatLab both *receives* and *returns* simple vectors - it does not
% need to perform any Java manipulation at all. Java objects CAN be returned
% in cases where it is easier or necessary to do so, but basic data types
% suffice for almost all applications.

function [] = SampleBotManager(botTime, recordFile)

    prepQASE;   % import the QASE library

    mlResults = cell(1, 1);

    try
        matLabBot = SampleMatLabObserverBot('MatLabObserver','female/athena');
        matLabBot.connect('127.0.0.1',-1,recordFile);

        tic;

        while(toc < botTime)
            if(matLabBot.waitingForMatLab == 1)
                mlParams = matLabBot.getMatLabParams;

                pos = mlParams(1);
                entPos = mlParams(2);
                dir = normc(entPos - pos);

                mlResults{1} = dir;
                matLabBot.setMatLabResults(mlResults);

                matLabBot.releaseFromMatLab;
            end

            pause(0.01);
        end
    catch
        disp 'An error occurred. Disconnecting bots...';
    end
    
    matLabBot.disconnect;
end
