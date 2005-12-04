
function [] = readDM2(filename)

prepQASE;
dm2p = DM2Parser(filename);
world = dm2p.getNextWorld;

while world ~= []

    % examine gamestate & extract data here %

    world = dm2p.getNextWorld;
end

dm2p.close;
