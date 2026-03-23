package de.omnp.meteoracle.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.omnp.meteoracle.application.domain.MoveCall.NotarizationMetadata;
import de.omnp.meteoracle.application.domain.ddd.DomainService;
import de.omnp.meteoracle.application.domain.vda4994.Post;
import de.omnp.meteoracle.application.port.KeyInterface;
import de.omnp.meteoracle.application.port.ScanReceiver;
import de.omnp.meteoracle.application.port.ScanReflection;
import de.omnp.meteoracle.application.port.ScanSender;
import de.omnp.meteoracle.jota.Wallet;


/**
 * Der Service ist die Implementierung des Inbound-Ports (UseCase).
 * Er orchestriert den Ablauf, ohne technische Details zu kennen. 
 * Der Service nutzt die SPI-Implementierungen genau so, wie der Controller den Service nutzt.
 */
@DomainService
public class TransactionService implements ScanReceiver, KeyInterface {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private NotarizationMetadata metadata;
    private Wallet wallet;



    private final ScanReflection reflection;
        
    // Der Service nutzt den Outbound-Port (das SPI)
    private final ScanSender sender;

    // Konstruktor-Injektion (Kein @Autowired nötig, da pure Java/Hexagonal)
    public TransactionService(ScanSender sender, ScanReflection reflection) {
        this.activate();
        this.sender = sender;
        this.reflection = reflection;
    }

    /**
     * Checks if the Scan's package ID is an owned Notarization object and need to get updated.
     */
    @Override
    public boolean checkIn(Post object) {        
        // TODO: objectAddress from the object associated with the address and wants to get updated
        String objectToUpdate = new String();
        objectToUpdate = reflection.reflectTransactions(this.metadata, object.getPackageId());
        if (objectToUpdate != null) {
            logger.info("Initializing the update process for object: " + objectToUpdate);
            if (this.sender.updateTransaction(object, this.wallet, this.metadata, objectToUpdate)) {
                return true;
            } else {
                return false;
            }     
        } else if (this.sender.sendTransaction(object, this.wallet, metadata)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Activation of the IOTA Wallet via Bech32 Private key starting with "iotaprivkey"
     */
    @Override
    public boolean activate() {
        this.wallet = new Wallet("iotaprivkey1qr8g2my5j4ghltrftjm797dvtdlc4s70688xasynknusxzt5rl5vkgkxnvg");
        // Benutzung im Service:
        this.metadata = new NotarizationMetadata(
            wallet.getAddress(),
            "https://api.testnet.iota.cafe:443",
            "0x0849cd69ff8946217824e40e41acfbcd8f1a3d77c3220ffa618ec57d55c74bed",
            "entry_notarization4994",
            "notarize_4994_scan",
            "update_4994_scan",
            "5000000"
        );
        return true;
    }
}
