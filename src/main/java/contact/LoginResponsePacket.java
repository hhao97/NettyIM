package contact;


public class LoginResponsePacket extends Packet {
    private Byte version;
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }

    @Override
    public Byte getVersion() {
        return version;
    }

    @Override
    public void setVersion(Byte version) {
        this.version = version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
