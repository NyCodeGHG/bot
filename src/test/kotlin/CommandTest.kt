/*
class CommandTest {

    @BeforeTest
    fun connectDB() {
        Database.connect(EmbeddedPostgres.start().postgresDatabase)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(VoteBotGuilds)
        }
    }

    @Test
    suspend fun `check prefixed normal command`() {
        val message = mockMessage {
            on { contentRaw }.thenReturn("v!test ${arguments.joinToString(" ")}")
        }

        val command = mockCommand {
            on { aliases }.thenReturn(listOf("test"))
        }

        ensureCommandCall(message, command, arguments)
    }

    @Test
    suspend fun `check mentioned normal command`() {
        val mention = selfMember.asMention()
        val message = mockMessage {
            on { contentRaw }.thenReturn("$mention test ${arguments.joinToString(" ")}")
        }

        val command = mockCommand {
            on { aliases }.thenReturn(listOf("test"))
        }

        ensureCommandCall(message, command, arguments)
    }

    @Test
    suspend fun `check prefixed sub command`() {
        val message = mockMessage {
            on { contentRaw }.thenReturn("v!test ${arguments.joinToString(" ")}")
        }

        val subCommand = mock<AbstractSubCommand> {
            on { permission }.thenReturn(Permission.ANY)
        }

        val command = mockCommand {
            on { aliases }.thenReturn(listOf("test"))
            on { commandAssociations }.thenReturn(mutableMapOf("sub" to subCommand))
        }

        ensureCommandCall(message, command, arguments.subList(1, arguments.size), subCommand)
    }

    @Test
    suspend fun `check mentioned sub command`() {
        val mention = selfMember.asMention()
        val message = mockMessage {
            on { contentRaw }.thenReturn("$mention test ${arguments.joinToString(" ")}")
        }

        val subCommand = mock<AbstractSubCommand> {
            on { permission }.thenReturn(Permission.ANY)
        }

        val command = mockCommand {
            on { aliases }.thenReturn(listOf("test"))
            on { commandAssociations }.thenReturn(mutableMapOf("sub" to subCommand))
        }

        ensureCommandCall(message, command, arguments.subList(1, arguments.size), subCommand)
    }

    @Test
    fun `check command error handling`() {
        val error = RuntimeException("Test error")
        val command = object : AbstractCommand() {
            override val aliases: List<String> = listOf("test")
            override val displayName: String
                get() = TODO("Not yet implemented")
            override val description: String
                get() = TODO("Not yet implemented")
            override val usage: String
                get() = TODO("Not yet implemented")
            override val permission: Permission = Permission.ANY
            override val category: CommandCategory
                get() = TODO("Not yet implemented")

            override suspend fun execute(context: Context) = throw error

        }

        val listener: Validator = mock()
        eventManager.register(listener)
        val message = mockMessage {
            on { contentRaw }.thenReturn("v!test ${arguments.joinToString(" ")}")
        }
        val event = GuildMessageReceivedEvent(jda, 200, message)

        client.registerCommands(command)
        client.onMessage(event)

        verify(listener).onError(argThat {
            this.error === error
        })
    }

    private interface Validator {
        @EventSubscriber
        fun onError(event: CommandErrorEvent)
    }

    private suspend fun ensureCommandCall(
            message: Message,
            command: AbstractCommand,
            arguments: List<String>,
            subCommand: AbstractSubCommand? = null
    ) {
        val event = GuildMessageReceivedEvent(jda, 200, message)
        client.registerCommands(command)
        client.onMessage(event)
        val actualCommand = subCommand ?: command
        verify(actualCommand).execute(argThat {
            arguments == args.toList() &&
                    client === this.commandClient &&
                    message === this.message &&
                    bot === this.bot &&
                    author === this.author
        })
    }

    private fun mockMessage(
            stubbing: KStubbing<Message>.() -> Unit
    ) =
            mock<Message> {
                on { this.author }.thenReturn(author)
                on { textChannel }.thenReturn(channel)
                on { contentRaw }.thenReturn("!test ${arguments.joinToString(" ")}")
                on { isWebhookMessage }.thenReturn(false)
                on { member }.thenReturn(selfMember)
                on { this.guild }.thenReturn(guild)
                on { jda }.thenReturn(jda)
                stubbing(this)
            }

    private fun mockCommand(
            stubbing: KStubbing<AbstractCommand>.() -> Unit
    ) = mock<AbstractCommand> {
        on { permission }.thenReturn(Permission.ANY)
        stubbing(this)
    }

    companion object {
        private lateinit var bot: VoteBot
        private val arguments = listOf("sub", "2", "3")
        private val influx = NopInfluxDBConnection()
        private lateinit var jda: JDA
        private lateinit var channel: TextChannel
        private lateinit var selfMember: Member
        private lateinit var guild: Guild
        private lateinit var client: CommandClientImpl
        private lateinit var author: User
        private val eventManager: IEventManager = AnnotatedEventManager(Dispatchers.Unconfined)

        @BeforeAll
        @JvmStatic
        @Suppress("unused")
        fun `setup mock objects`() {
            bot = mock {
                on { this.influx }.thenReturn(influx)
                on { eventManager }.thenReturn(eventManager)
            }
            client = CommandClientImpl(bot, Constants.prefix, Dispatchers.Unconfined)
            jda = mock()
            channel = mock {
                on { sendTyping() }.thenReturn(EmptyRestAction<Void>())
            }
            selfMember = mock {
                on { id }.thenReturn("123456789")
            }
            guild = mock {
                on { this.selfMember }.thenReturn(selfMember)
            }
            author = mock {
                on { isBot }.thenReturn(false)
                on { isFake }.thenReturn(false)
            }
        }
    }
}

private class EmptyRestAction<T> : RestAction<T> {
    override fun submit(shouldQueue: Boolean): CompletableFuture<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun complete(shouldQueue: Boolean): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getJDA(): JDA {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun queue(success: Consumer<in T?>?, failure: Consumer<in Throwable>?) = success?.accept(null)
            ?: Unit

    override fun setCheck(checks: BooleanSupplier?): RestAction<T> = this

}*/