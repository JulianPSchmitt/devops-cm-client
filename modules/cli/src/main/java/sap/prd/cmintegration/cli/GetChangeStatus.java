package sap.prd.cmintegration.cli;

import static java.lang.String.format;
import static sap.prd.cmintegration.cli.Commands.Helpers.getChangeId;
import static sap.prd.cmintegration.cli.Commands.Helpers.getCommandName;
import static sap.prd.cmintegration.cli.Commands.Helpers.getHost;
import static sap.prd.cmintegration.cli.Commands.Helpers.getPassword;
import static sap.prd.cmintegration.cli.Commands.Helpers.getUser;
import static sap.prd.cmintegration.cli.Commands.Helpers.handleHelpOption;
import static sap.prd.cmintegration.cli.Commands.Helpers.helpRequested;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import sap.ai.st.cm.plugins.ciintegration.odataclient.CMODataChange;
import sap.ai.st.cm.plugins.ciintegration.odataclient.CMODataClient;

@CommandDescriptor(name = "is-change-in-development")
class GetChangeStatus extends Command {

    private String changeId;

    GetChangeStatus(String host, String user, String password, String changeId) {
        super(host, user, password);
        this.changeId = changeId;
    }

    @Override
    void execute() throws Exception {
        try (CMODataClient client = ClientFactory.getInstance().newClient(host, user, password)) {
            CMODataChange change = client.getChange(changeId);
            System.out.println(change.isInDevelopment());
        }
    }

    public final static void main(String[] args) throws Exception {

        Options options = new Options();
        Commands.Helpers.addStandardParameters(options);

        if(helpRequested(args)) {
            handleHelpOption(format("%s [options] <changeId>", getCommandName(GetChangeStatus.class)),
                    "Returns 'true' if the transport specified by <changeId> is modifiable. Otherwise 'false'.",options); return;
        }

        CommandLine commandLine = new DefaultParser().parse(options, args);

        new GetChangeStatus(
                getHost(commandLine),
                getUser(commandLine),
                getPassword(commandLine),
                getChangeId(commandLine)).execute();
    }
}
