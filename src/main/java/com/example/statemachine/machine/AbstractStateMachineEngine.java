package com.example.statemachine.machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.GenericTypeResolver;
import org.squirrelframework.foundation.fsm.StateMachine;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.StateMachineConfiguration;

/**
 * 抽象状态机引擎，定义状态机执行的过程
 *
 * @param <T> 状态机对象
 * @param <S> 需要变迁的状态
 * @param <E> event，执行状态变更的事件
 * @param <C> context，状态机执行的上下文，通常包含一些外部参数
 * @author Sean
 * @since 2019-04-04
 */
public abstract class AbstractStateMachineEngine<T extends StateMachine<T, S, E, C>, S, E, C> implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStateMachineEngine.class);

    private static final String ERROR_MESSAGE_TERMINATED = "The state machine is already terminated.";

    protected ApplicationContext applicationContext;

    protected StateMachineBuilder<T, S, E, C> stateMachineBuilder = null;

//    @Autowired
//    private StateMachineProperties stateMachineProperties;
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @Autowired
//    private LepinCommonProperties lepinCommonProperties;

    @SuppressWarnings({"unchecked"})
    public AbstractStateMachineEngine() {
        //识别泛型参数
        Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractStateMachineEngine.class);
        Class<T> stateMachineType = (Class<T>) genericTypes[0];
        Class<S> stateType = (Class<S>) genericTypes[1];
        Class<E> eventType = (Class<E>) genericTypes[2];
        Class<C> contextType = (Class<C>) genericTypes[3];
        stateMachineBuilder = StateMachineBuilderFactory.create(stateMachineType, stateType, eventType, contextType, ApplicationContext.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获得状态机对象的初始状态
     *
     * @param context 上下文环境
     * @return 实体的当前状态
     */
    protected abstract S getInitialStatus(C context);

    /**
     * 获得分布式锁的id
     *
     * @param context 上下文环境
     * @return 返回分布式锁id
     */
    protected String getDistributionLockId(C context) {
        return "";
    }

    public void fire(E event, C context) {

        this.doFire(event, context);


//        String lockId = this.getDistributionLockId(context);
//        //锁为空则直接执行
//        if (StringUtils.isBlank(lockId) || this.redissonClient == null) {
//            this.doFire(event, context);
//        } else {
//            RLock lock = this.redissonClient.getLock(lockId);
//            boolean hasLocked = false;
//            try {
//                hasLocked = lock.tryLock(this.lepinCommonProperties.getDefaultLockWait(), this.lepinCommonProperties.getDefaultLockLease(), TimeUnit.SECONDS);
//                doFire(event, context);
//            } catch (Exception e) {
//                LOGGER.error(MessageFormat.format("Filed to fire state machine with event [{0}] and context [{1}]!", event, JSON.toJSON(context)), e);
//                if (e instanceof TransitionException) {
//                    throw new Exception(RestResponse.BIZ_FAIL, ((TransitionException) e).getTargetException());
//                }
//                throw new BizException(RestResponse.BIZ_FAIL, e);
//            } finally {
//                if (hasLocked && lock.isLocked()) {
//                    try {
//                        lock.unlock();
//                    } catch (Exception e) {
//                        LOGGER.error("Fail to unlock lock " + lock.getName() + " due to exception found.", e);
//                    }
//                }
//            }
//        }
    }

    protected T createMachineInstance(S initialState, StateMachineConfiguration configuration) {
        return this.stateMachineBuilder.newStateMachine(
                initialState,
                configuration,
                this.applicationContext);
    }

    protected void doFire(E event, C context) {
        //获得初始状态
        S initialState = this.getInitialStatus(context);
        //构建配置
//        if (this.stateMachineProperties == null) {
//            this.stateMachineProperties = new StateMachineProperties();
//        }
        StateMachineConfiguration configuration = StateMachineConfiguration.create()
                .enableAutoStart(true)
                .enableDebugMode(true)
                .enableAutoTerminate(true)
                .enableDataIsolate(true)
                .enableDelegatorMode(true)
                .enableRemoteMonitor(true);
        //构建状态机
        T stateMachine = this.createMachineInstance(initialState, configuration);

        //构建事务处理
//        PlatformTransactionManager transactionManager = null;
//        if (this.stateMachineProperties.isEnableTransactionManagement()) {
//            try {
//                transactionManager = this.applicationContext.getBean(PlatformTransactionManager.class);
//            } catch (BeansException ex) {
//                LOGGER.debug("Fail to get transaction manager bean from application context", ex);
//            }
//        }
//        TransactionStatus transactionStatus = null;
//        if (transactionManager != null) {
//            DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
//            transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//            transactionStatus = transactionManager.getTransaction(transactionDefinition);
//        }
        try {
            //触发
            stateMachine.fire(event, context);
//            if (stateMachine.isError()) {
//                throw stateMachine.getLastException();
//            } else {
//                if (transactionManager != null) {
//                    transactionManager.commit(transactionStatus);
//                }
//            }
        } catch (Exception ex) {
            LOGGER.error("Fail to fire event " + event, ex);
//            //发生异常则回滚
//            if (transactionManager != null) {
//                transactionManager.rollback(transactionStatus);
//            }
//
//            //如果当期状态为TERMINATED，直接抛异常
//            if (StringUtils.contains(ex.getMessage(), ERROR_MESSAGE_TERMINATED)) {
//                LOGGER.error("The state machine of event {} is already terminated.", JSON.toJSON(event));
//                throw new BizException(201022);
//            } else {
//                throw ex;
//            }
        }
    }
}
