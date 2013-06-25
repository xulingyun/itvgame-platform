package cn.ohyeah.itvgame.protocolv2;

import cn.ohyeah.stb.utils.ByteBuffer;

/**
 * 默认协议处理器
 * @author maqian
 * @version 1.0
 */
public class DefaultProcessor implements IProcessor {
	private static final IProcessor recordProcessor = new RecordProcessor();
	private static final IProcessor attainmentProcessor = new AttainmentProcessor();
	private static final IProcessor propProcessor = new PropProcessor();
	private static final IProcessor subscribeProcessor = new SubscribeProcessor();
	private static final IProcessor purchaseProcessor = new PurchaseProcessor();
	private static final IProcessor accountProcessor = new AccountProcessor();
	private static final IProcessor sysServProcessor = new SysServProcessor();
	
	public void processRequest(ProcessorContext context, ByteBuffer req) {
		HeadWrapper headWrapper = new HeadWrapper(req.readInt());
        context.setHeadWrapper(headWrapper);
		//从版本2开始, 跳过length字段
        if (headWrapper.getVersion() >= 2) {
		    req.readInt();
        }
		if (headWrapper.getVersion() > Constant.PROTOCOL_VERSION) {
			String msg = "协议版本不匹配,当前版本version="+Constant.PROTOCOL_VERSION
			+", 请求版本version="+headWrapper.getVersion();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
		IProcessor processor = null;
		switch (headWrapper.getTag()) {
		case Constant.PROTOCOL_TAG_RECORD: 
			processor = recordProcessor;
			break;
		case Constant.PROTOCOL_TAG_ATTAINMENT:
			processor = attainmentProcessor;
			break;
		case Constant.PROTOCOL_TAG_PROP: 
			processor = propProcessor;
			break;
		case Constant.PROTOCOL_TAG_SUBSCRIBE:
			processor = subscribeProcessor;
			break;
		case Constant.PROTOCOL_TAG_PURCHASE: 
			processor = purchaseProcessor;
			break;
		case Constant.PROTOCOL_TAG_ACCOUNT:
			processor = accountProcessor;
			break;
		case Constant.PROTOCOL_TAG_SYS_SERV:
			processor = sysServProcessor;
			break;
		default: 
			String msg = "无效的协议标识, tag="+headWrapper.getTag();
			context.setErrorCode(Constant.EC_INVALID_TAG);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
		context.setProcessor(processor);
		processor.processRequest(context, req);
	}
	
	public void processResponse(ProcessorContext context, ByteBuffer rsp) {
        HeadWrapper headWrapper = context.getHeadWrapper();
        rsp.writeInt(headWrapper.getHead());
        //从版本2开始, 写入length字段
        if (headWrapper.getVersion() >= 2) {
            rsp.writeInt(0);
        }
		if (context.getErrorCode() == 0) {
            rsp.writeInt(0);
			context.getProcessor().processResponse(context, rsp);
		}
		else {
            rsp.writeInt(context.getErrorCode());
            rsp.writeUTF(context.getMessage());
		}
	}
}
