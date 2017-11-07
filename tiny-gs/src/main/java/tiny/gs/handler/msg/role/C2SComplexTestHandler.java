package tiny.gs.handler.msg.role;

import com.google.protobuf.InvalidProtocolBufferException;

import auto.proto.Enum;
import auto.proto.L2GMessageProto.L2GMessage;
import auto.proto.RoleProto.C2SComplexTest;
import tiny.gs.handler.MsgHandler;
import tiny.gs.handler.ProtocolHandler;

@MsgHandler(msgName = "C2SComplexTest", msgKey = Enum.PROTO_KEY.C2SComplexTest_Key_VALUE)
public class C2SComplexTestHandler implements ProtocolHandler{

	@Override
	public void process(L2GMessage l2g) throws InvalidProtocolBufferException {
		// TODO Auto-generated method stub
		C2SComplexTest c2s = C2SComplexTest.parseFrom(l2g.getContentMsg());
		System.err.println("rec " + c2s);
	}
}
